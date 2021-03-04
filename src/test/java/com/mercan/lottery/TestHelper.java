package com.mercan.lottery;

import com.mercan.lottery.dto.TicketLineResult;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.entity.TicketLine;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    private static final Integer TEST_RESULT_VALUE = 10;
    private static final Integer TEST_LINE_VALUE = 1;
    private static final Integer TEST_LINE_COUNT = 1;

    public static Ticket createTicket(int numberOfLines) {
        List<TicketLine> ticketLines = createTicketLineList(numberOfLines);
        return Ticket.builder().id(1).ticketLines(ticketLines).build();
    }

    public static TicketLine createTicketLine() {
        return new TicketLine(TEST_LINE_VALUE, TEST_LINE_VALUE, TEST_LINE_VALUE);
    }

    public static List<TicketLine> createTicketLineList(int numberOfLines) {
        List<TicketLine> ticketLines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            ticketLines.add(createTicketLine());
        }
        return ticketLines;
    }

    public static TicketResult createTicketResult() {
        Ticket createdTicket = createTicket(TEST_LINE_COUNT);
        List<TicketLineResult> createdTicketLineResultList = createLineResultList(TEST_LINE_COUNT);
        return TicketResult.builder().id(createdTicket.getId()).results(createdTicketLineResultList).build();
    }

    private static List<TicketLineResult> createLineResultList(int numberOfLines) {
        List<TicketLineResult> ticketLineResults = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            ticketLineResults.add(createLineResult());
        }
        return ticketLineResults;
    }

    private static TicketLineResult createLineResult() {
        return TicketLineResult.builder().line(createTicketLine()).result(TEST_RESULT_VALUE).build();
    }

}
