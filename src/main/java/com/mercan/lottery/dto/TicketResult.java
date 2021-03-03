package com.mercan.lottery.dto;

import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.entity.TicketLine;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResult {

    private List<LineResult> results = new ArrayList<>();
    private Ticket ticket;

    public TicketResult(Ticket ticket, LineResultCalculatorStrategy lineResultCalculatorStrategy) {
        for (TicketLine line : ticket.getTicketLines()) {
            int result = lineResultCalculatorStrategy.calculateResult(line);
            results.add(new LineResult(line, result));
        }
        this.ticket = ticket;
    }
}