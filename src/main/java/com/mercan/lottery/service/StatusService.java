package com.mercan.lottery.service;


import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final LineResultCalculatorStrategy lineResultCalculatorStrategy;
    private final TicketRepository ticketRepository;


    public TicketResult checkStatus(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info("ticket is not found for id:{}", ticketId);
            return new TicketNotFoundException("ticket is not found for id " + ticketId);
        });
        ticket.setChecked(true);
        ticketRepository.save(ticket);
        return new TicketResult(ticket, lineResultCalculatorStrategy);
    }
}
