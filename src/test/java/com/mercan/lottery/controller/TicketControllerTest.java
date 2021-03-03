package com.mercan.lottery.controller;

import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.exception.TicketCheckedException;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mercan.lottery.TestHelper.createTicket;
import static com.mercan.lottery.TestHelper.createTicketResult;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
class TicketControllerTest {
    private static final String TICKET_ENDPOINT = "/api/lottery/ticket";
    private static final String CHECK_TICKET_ENDPOINT = TICKET_ENDPOINT + "/check";

    @MockBean
    TicketService ticketService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void create_ticket_expect_success() throws Exception {

        //given
        int requestedNumberOfLines = 2;
        Ticket expectedTicket = createTicket(requestedNumberOfLines);
        when(ticketService.generateTicket(requestedNumberOfLines)).thenReturn(expectedTicket);

        //when
        this.mockMvc
                .perform(post(TICKET_ENDPOINT + "?numberOfLines=" + requestedNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketLines", hasSize(2)))
                .andExpect(jsonPath("$.checked", is(false)));

        verify(ticketService).generateTicket(requestedNumberOfLines);
    }

    @Test
    public void create_ticket_expect_exception_when_no_lines_provided() throws Exception {

        //given

        //when
        this.mockMvc
                .perform(post(TICKET_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("Required Integer parameter 'numberOfLines' is not present")));

        verify(ticketService, never()).generateTicket(anyInt());
    }

    @Test
    public void create_ticket_expect_exception_when_invalid_lines_provided() throws Exception {

        //given
        int invalidNumberOfLines = -1;

        //when
        this.mockMvc
                .perform(post(TICKET_ENDPOINT + "?numberOfLines=" + invalidNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("numberOfLines can not be lower than 1. Input value:{" + invalidNumberOfLines + "}")));

        verify(ticketService, never()).generateTicket(invalidNumberOfLines);
    }

    @Test
    public void update_ticket_expect_success() throws Exception {

        //given
        int requestedNumberOfLines = 2;
        long validTicketId = 3;
        Ticket expectedTicket = createTicket(requestedNumberOfLines);
        when(ticketService.updateTicket(validTicketId, requestedNumberOfLines)).thenReturn(expectedTicket);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?numberOfLines=" + requestedNumberOfLines + "&ticketId=" + validTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketLines", hasSize(requestedNumberOfLines)))
                .andExpect(jsonPath("$.checked", is(false)));

        verify(ticketService).updateTicket(validTicketId, requestedNumberOfLines);
    }

    @Test
    public void update_ticket_expect_exception_when_no_lines_provided() throws Exception {
        //given
        long validTicketId = 3;

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?ticketId=" + validTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("Required Integer parameter 'numberOfLines' is not present")));

        verify(ticketService, never()).updateTicket(any(), anyInt());
    }

    @Test
    public void update_ticket_expect_exception_when_invalid_lines_provided() throws Exception {
        //given
        long validTicketId = 1;
        long invalidNumberOfLines = -1;

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?numberOfLines=" + invalidNumberOfLines + "&ticketId=" + validTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("numberOfLines can not be lower than 1. Input value:{" + invalidNumberOfLines + "}")));

        verify(ticketService, never()).updateTicket(any(), anyInt());
    }

    @Test
    public void update_ticket_expect_exception_when_no_ticket_provided() throws Exception {
        //given
        long validNumberOfLines = 3;

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?numberOfLines=" + validNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("Required Long parameter 'ticketId' is not present")));

        verify(ticketService, never()).updateTicket(any(), anyInt());
    }

    @Test
    public void update_ticket_expect_exception_when_ticket_is_not_found() throws Exception {

        //given
        long invalidTicketId = 3;
        int validNumberOfLines = 3;
        doThrow(new TicketNotFoundException("ticket is not found for id " + invalidTicketId)).when(ticketService).updateTicket(invalidTicketId, validNumberOfLines);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?numberOfLines=" + validNumberOfLines + "&ticketId=" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket is not found for id " + invalidTicketId)));


        verify(ticketService).updateTicket(invalidTicketId, validNumberOfLines);
    }

    @Test
    public void update_ticket_expect_exception_when_ticket_checked_before() throws Exception {

        //given
        long invalidTicketId = 3;
        int validNumberOfLines = 3;

        doThrow(new TicketCheckedException("ticket with id:" + invalidTicketId + " has been checked before.")).when(ticketService).updateTicket(invalidTicketId, validNumberOfLines);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "?numberOfLines=" + validNumberOfLines + "&ticketId=" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket with id:" + invalidTicketId + " has been checked before.")));


        verify(ticketService).updateTicket(invalidTicketId, validNumberOfLines);
    }


    @Test
    public void check_ticket_expect_success() throws Exception {

        //given
        long requestedTicketId = 2;
        TicketResult expectedTicketResult = createTicketResult();
        when(ticketService.checkTicket(requestedTicketId)).thenReturn(expectedTicketResult);

        //when
        this.mockMvc
                .perform(get(CHECK_TICKET_ENDPOINT + "?ticketId=" + requestedTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket.checked", is(false)))
                .andExpect(jsonPath("$.results", not(empty())));

        verify(ticketService).checkTicket(requestedTicketId);
    }

    @Test
    public void check_ticket_expect_exception_when_ticket_id_is_not_provided() throws Exception {
        //when
        this.mockMvc
                .perform(get(CHECK_TICKET_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("Required Long parameter 'ticketId' is not present")));


        verify(ticketService, never()).checkTicket(anyLong());
    }

    @Test
    public void check_ticket_expect_exception_when_ticket_is_not_found() throws Exception {

        //given
        long invalidTicketId = 2;
        doThrow(new TicketNotFoundException("ticket is not found for id " + invalidTicketId)).when(ticketService).checkTicket(invalidTicketId);

        //when
        this.mockMvc
                .perform(get(CHECK_TICKET_ENDPOINT + "?ticketId=" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket is not found for id " + invalidTicketId)));


        verify(ticketService).checkTicket(invalidTicketId);
    }


}