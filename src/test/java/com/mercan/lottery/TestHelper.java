package com.mercan.lottery;

import com.mercan.lottery.dto.LineResult;
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
        List<LineResult> createdLineResultList = createLineResultList(TEST_LINE_COUNT);
        return TicketResult.builder().ticket(createdTicket).results(createdLineResultList).build();
    }

    private static List<LineResult> createLineResultList(int numberOfLines) {
        List<LineResult> lineResults = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            lineResults.add(createLineResult());
        }
        return lineResults;
    }

    private static LineResult createLineResult() {
        return LineResult.builder().line(createTicketLine()).result(TEST_RESULT_VALUE).build();
    }

}
