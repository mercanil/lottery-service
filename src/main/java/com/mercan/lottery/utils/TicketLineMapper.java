package com.mercan.lottery.utils;

import com.mercan.lottery.dto.TicketLineResult;
import com.mercan.lottery.entity.TicketLine;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;

public class TicketLineMapper {
    public static TicketLineResult toLineResult(TicketLine line, LineResultCalculatorStrategy lineResultCalculatorStrategy) {
        int result = lineResultCalculatorStrategy.calculateResult(line);
        return new TicketLineResult(line, result);
    }
}
