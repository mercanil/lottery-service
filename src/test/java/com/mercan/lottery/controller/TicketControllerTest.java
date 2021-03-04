package com.mercan.lottery.controller;

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

import java.util.Collections;
import java.util.List;

import static com.mercan.lottery.TestHelper.createTicket;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
class TicketControllerTest {
    private static final String TICKET_ENDPOINT = "/ticket";

    @MockBean
    TicketService ticketService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void create_ticket_expect_success() throws Exception {

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
    void create_ticket_expect_exception_when_no_lines_provided() throws Exception {

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
    void create_ticket_expect_exception_when_invalid_lines_provided() throws Exception {

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
    void get_ticket_expect_success() throws Exception {

        //given
        int requestedNumberOfLines = 2;
        Ticket expectedTicket = createTicket(requestedNumberOfLines);
        when(ticketService.getTicket(expectedTicket.getId())).thenReturn(expectedTicket);

        //when
        this.mockMvc
                .perform(get(TICKET_ENDPOINT + "/" + expectedTicket.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketLines", hasSize(requestedNumberOfLines)))
                .andExpect(jsonPath("$.checked", is(false)));

        verify(ticketService).getTicket(expectedTicket.getId());
    }

    @Test
    void get_ticket_expect_exception_when_ticket_is_not_found() throws Exception {

        //given
        long invalidTicketId = 3;
        doThrow(new TicketNotFoundException("ticket is not found for id " + invalidTicketId)).when(ticketService).getTicket(invalidTicketId);

        //when
        this.mockMvc
                .perform(get(TICKET_ENDPOINT + "/" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket is not found for id " + invalidTicketId)));


        verify(ticketService).getTicket(invalidTicketId);
    }


    @Test
    void get_ticket_list_expect_success() throws Exception {

        //given
        int requestedNumberOfLines = 2;
        Ticket expectedTicket = createTicket(requestedNumberOfLines);
        List<Ticket> expectedResponse = Collections.singletonList(expectedTicket);
        when(ticketService.getAllTickets()).thenReturn(expectedResponse);

        //when
        this.mockMvc
                .perform(get(TICKET_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedResponse.size())))
                .andExpect(jsonPath("[0].checked", is(false)))
                .andExpect(jsonPath("[0].ticketLines", hasSize(requestedNumberOfLines)));
        verify(ticketService).getAllTickets();
    }


    @Test
    void update_ticket_expect_success() throws Exception {

        //given
        int requestedNumberOfLines = 2;
        long validTicketId = 3;
        Ticket expectedTicket = createTicket(requestedNumberOfLines);
        when(ticketService.updateTicket(validTicketId, requestedNumberOfLines)).thenReturn(expectedTicket);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "/" + validTicketId + "?numberOfLines=" + requestedNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketLines", hasSize(requestedNumberOfLines)))
                .andExpect(jsonPath("$.checked", is(false)));

        verify(ticketService).updateTicket(validTicketId, requestedNumberOfLines);
    }

    @Test
    void update_ticket_expect_exception_when_no_lines_provided() throws Exception {
        //given
        long validTicketId = 3;

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "/" + validTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("Required Integer parameter 'numberOfLines' is not present")));

        verify(ticketService, never()).updateTicket(any(), anyInt());
    }

    @Test
    void update_ticket_expect_exception_when_invalid_lines_provided() throws Exception {
        //given
        long validTicketId = 1;
        long invalidNumberOfLines = -1;

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "/" + validTicketId + "?numberOfLines=" + invalidNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.errors[0]", is("numberOfLines can not be lower than 1. Input value:{" + invalidNumberOfLines + "}")));

        verify(ticketService, never()).updateTicket(any(), anyInt());
    }

    @Test
    void update_ticket_expect_exception_when_ticket_is_not_found() throws Exception {

        //given
        long invalidTicketId = 3;
        int validNumberOfLines = 3;
        doThrow(new TicketNotFoundException("ticket is not found for id " + invalidTicketId)).when(ticketService).updateTicket(invalidTicketId, validNumberOfLines);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "/" + invalidTicketId + "?numberOfLines=" + validNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket is not found for id " + invalidTicketId)));


        verify(ticketService).updateTicket(invalidTicketId, validNumberOfLines);
    }

    @Test
    void update_ticket_expect_exception_when_ticket_checked_before() throws Exception {

        //given
        long validTicketId = 3;
        int validNumberOfLines = 3;

        doThrow(new TicketCheckedException("ticket with id:" + validTicketId + " has been checked before.")).when(ticketService).updateTicket(validTicketId, validNumberOfLines);

        //when
        this.mockMvc
                .perform(put(TICKET_ENDPOINT + "/" + validTicketId + "?numberOfLines=" + validNumberOfLines)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket with id:" + validTicketId + " has been checked before.")));


        verify(ticketService).updateTicket(validTicketId, validNumberOfLines);
    }

    @Test
    void delete_ticket_expect_success() throws Exception {

        //given
        long validTicketId = 1L;
        doNothing().when(ticketService).delete(validTicketId);

        //when
        this.mockMvc
                .perform(delete(TICKET_ENDPOINT + "/" + validTicketId )
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());


        verify(ticketService).delete(validTicketId);
    }


    @Test
    void delete_ticket_expect_exception_when_ticket_does_not_exist() throws Exception {

        //given
        long invalidTicketId = 3;

        doThrow(new TicketCheckedException("ticket with id:" + invalidTicketId + " has been checked before.")).when(ticketService).delete(invalidTicketId);

        //when
        this.mockMvc
                .perform(delete(TICKET_ENDPOINT + "/" + invalidTicketId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reasonCode", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.errors[0]", is("ticket with id:" + invalidTicketId + " has been checked before.")));


        verify(ticketService).delete(invalidTicketId);
    }
}