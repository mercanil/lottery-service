package com.mercan.lottery.controller;

import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.service.StatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mercan.lottery.TestHelper.createTicketResult;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StatusController.class)
@AutoConfigureMockMvc
class StatusControllerTest {

    private static final String STATUS_ENDPOINT = "/status";

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    StatusService statusService;

    @Test
    void check_ticket_expect_success() throws Exception {

        //given
        long requestedTicketId = 2;
        TicketResult expectedTicketResult = createTicketResult();
        when(statusService.checkStatus(requestedTicketId)).thenReturn(expectedTicketResult);

        //when
        this.mockMvc
                .perform(put(STATUS_ENDPOINT + "/" + requestedTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket.checked", is(false)))
                .andExpect(jsonPath("$.results", not(empty())));

        verify(statusService).checkStatus(requestedTicketId);
    }

    @Test
    void check_ticket_expect_exception_when_ticket_is_not_found() throws Exception {

        //given
        long invalidTicketId = 2;
        doThrow(new TicketNotFoundException("ticket is not found for id " + invalidTicketId)).when(statusService).checkStatus(invalidTicketId);

        //when
        this.mockMvc
                .perform(put(STATUS_ENDPOINT + "/" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket is not found for id " + invalidTicketId)));


        verify(statusService).checkStatus(invalidTicketId);
    }
}