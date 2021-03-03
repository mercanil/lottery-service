package com.mercan.lottery.entity;

import com.mercan.lottery.service.strategy.LineGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketFactory {

    private final LineGeneratorStrategy lineGenerator;

    public Ticket generateTicket(int numLines) {
        List<TicketLine> lines = new ArrayList<>(numLines);
        for (int i = 0; i < numLines; i++) {
            lines.add(lineGenerator.generateLine());
        }
        return new Ticket(lines);
    }
}
