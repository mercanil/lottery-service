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
    private long id;
    private boolean isChecked;

    public TicketResult(Ticket ticket ,List<LineResult> lineResults ) {
        this.results = lineResults;
        this.id = ticket.getId();
        this.isChecked = ticket.isChecked();
    }
}