package com.mercan.lottery.service;

import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.entity.TicketFactory;
import com.mercan.lottery.exception.TicketCheckedException;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineGeneratorStrategy;
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

    private static final String TICKET_NOT_FOUND = "ticket is not found for id:{%d}";
    private static final String TICKET_ALREADY_CHECKED_FOUND = "ticket with id:{%d} has been checked before.";

    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info(String.format(TICKET_NOT_FOUND, ticketId));
            return new TicketNotFoundException(String.format(TICKET_NOT_FOUND, ticketId));
        });
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket generateTicket(Integer numberLines) {
        Ticket lotteryTicket = ticketFactory.generateTicket(numberLines);
        return ticketRepository.save(lotteryTicket);

    }

    public Ticket updateTicket(Long ticketId, Integer numberLines) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info(String.format(TICKET_NOT_FOUND, ticketId));
            return new TicketNotFoundException(String.format(TICKET_NOT_FOUND, ticketId));
        });
        if (ticket.isChecked()) {
            log.info(String.format(TICKET_ALREADY_CHECKED_FOUND, ticketId));
            throw new TicketCheckedException(String.format(TICKET_ALREADY_CHECKED_FOUND, ticketId));
        }
        for (int i = 0; i < numberLines; i++) {
            ticket.addLine(lineGeneratorStrategy.generateLine());
        }
        return ticketRepository.save(ticket);
    }

    public void delete(Long ticketId) {
        ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info(String.format(TICKET_NOT_FOUND, ticketId));
            return new TicketNotFoundException(String.format(TICKET_NOT_FOUND, ticketId));
        });

        ticketRepository.deleteById(ticketId);
    }
}
