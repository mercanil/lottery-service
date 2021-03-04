package com.mercan.lottery.dto;

import com.mercan.lottery.entity.Ticket;
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

    private List<TicketLineResult> results = new ArrayList<>();
    private long id;
    private boolean isChecked;

    public TicketResult(Ticket ticket ,List<TicketLineResult> ticketLineResults) {
        this.results = ticketLineResults;
        this.id = ticket.getId();
        this.isChecked = ticket.isChecked();
    }
}