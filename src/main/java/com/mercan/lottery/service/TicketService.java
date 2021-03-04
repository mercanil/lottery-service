package com.mercan.lottery.service;

import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.entity.TicketFactory;
import com.mercan.lottery.exception.TicketCheckedException;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineGeneratorStrategy;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final LineGeneratorStrategy lineGeneratorStrategy;
    private final LineResultCalculatorStrategy lineResultCalculatorStrategy;


    public Ticket generateTicket(Integer numberLines) {
        Ticket lotteryTicket = ticketFactory.generateTicket(numberLines);
        return ticketRepository.save(lotteryTicket);

    }

    public Ticket updateTicket(Long ticketId, Integer numberLines) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info("ticket is not found for id:{}", ticketId);
            return new TicketNotFoundException("ticket is not found for id " + ticketId);
        });
        if (ticket.isChecked()) {
            log.info("ticket id : {} already checked. Unable to update", ticketId);
            throw new TicketCheckedException("ticket with id:" + ticketId + " has been checked before.");
        }
        for (int i = 0; i < numberLines; i++) {
            ticket.addLine(lineGeneratorStrategy.generateLine());
        }
        return ticketRepository.save(ticket);
    }

    public TicketResult checkTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info("ticket is not found for id:{}", ticketId);
            return new TicketNotFoundException("ticket is not found for id " + ticketId);
        });
        ticket.setChecked(true);
        ticketRepository.save(ticket);
        return new TicketResult(ticket, lineResultCalculatorStrategy);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
