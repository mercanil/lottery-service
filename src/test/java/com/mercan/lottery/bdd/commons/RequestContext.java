package com.mercan.lottery.bdd.commons;

import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.dto.ApiError;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RequestContext {
    private static final String TICKET = "ticket";
    private static final String TICKET_RESULT = "ticket-result";
    public static Map<String, Object> context = new HashMap<>();


    public static void storeTicketResponse(ResponseEntity<Ticket> ticket) {
        context.put(TICKET, ticket);
    }

    public static void storeTicketResultResponse(ResponseEntity<TicketResult> ticketResult) {
        context.put(TICKET_RESULT, ticketResult);
    }

    public static void storeErrorResponse(ResponseEntity error) {
        context.put(TICKET, error);
    }

    public static ResponseEntity<Ticket> getTicketResponse() {
        return (ResponseEntity<Ticket>) context.get(TICKET);
    }

    public static ResponseEntity<TicketResult> getTicketResultResponse() {
        return (ResponseEntity<TicketResult>) context.get(TICKET_RESULT);
    }

    public static ResponseEntity<ApiError> getTicketError() {
        return (ResponseEntity<ApiError>) context.get(TICKET);
    }
}

