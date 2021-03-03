package com.mercan.lottery.service.strategy;

import com.mercan.lottery.entity.TicketLine;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class ResultCalculator implements LineResultCalculatorStrategy {
    @Override
    public int calculateResult(TicketLine line) {
        int result = 0;
        int[] lineNumbers = line.getNumbers();
        if (Arrays.stream(lineNumbers).sum() == 2) {
            result = 10;
        } else if (Arrays.stream(lineNumbers).distinct().count() == 1) {
            result = 5;
        } else if (lineNumbers[0] != lineNumbers[1] && lineNumbers[0] != lineNumbers[2]) {
            result = 1;
        }
        return result;
    }
}
