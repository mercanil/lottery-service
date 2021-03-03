package com.mercan.lottery.service.strategy;

import com.mercan.lottery.entity.TicketLine;

public interface LineResultCalculatorStrategy {
    int calculateResult(TicketLine line);
}
