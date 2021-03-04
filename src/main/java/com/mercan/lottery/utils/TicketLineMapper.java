package com.mercan.lottery.utils;

import com.mercan.lottery.dto.LineResult;
import com.mercan.lottery.entity.TicketLine;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;

public class TicketLineMapper {
    public static LineResult toLineResult(TicketLine line, LineResultCalculatorStrategy lineResultCalculatorStrategy) {
        int result = lineResultCalculatorStrategy.calculateResult(line);
        return new LineResult(line, result);
    }
}
