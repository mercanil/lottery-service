package com.mercan.lottery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean isChecked;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketLine> ticketLines;

    @Version
    private long version;

    public Ticket(long id, boolean isChecked, List<TicketLine> ticketLines) {
        this.id = id;
        this.isChecked = isChecked;
        this.ticketLines = ticketLines;
    }

    public Ticket(List<TicketLine> lines) {
        this.ticketLines = lines;
    }

    public void addLine(TicketLine line) {
        if (ticketLines == null) {
            this.ticketLines = new ArrayList<>();
        }
        this.ticketLines.add(line);
    }
}