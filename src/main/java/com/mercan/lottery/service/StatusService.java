package com.mercan.lottery.service;


import com.mercan.lottery.dto.TicketLineResult;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import com.mercan.lottery.utils.TicketLineMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private static final String TICKET_NOT_FOUND = "ticket is not found for id:{%d}";
    private final LineResultCalculatorStrategy lineResultCalculatorStrategy;
    private final TicketRepository ticketRepository;


    public TicketResult checkStatus(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            log.info(String.format(TICKET_NOT_FOUND, ticketId));
            return new TicketNotFoundException(String.format(TICKET_NOT_FOUND, ticketId));
        });
        ticket.setChecked(true);
        ticketRepository.save(ticket);

        List<TicketLineResult> ticketLineResult = ticket.getTicketLines()
                .stream()
                .map(ticketLine -> TicketLineMapper.toLineResult(ticketLine, lineResultCalculatorStrategy))
                .sorted(Comparator.comparing(TicketLineResult::getResult))
                .collect(Collectors.toList());
        return new TicketResult(ticket, ticketLineResult);
    }
}
