package com.mercan.lottery.service.strategy;

import com.mercan.lottery.entity.TicketLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomValueLineGenerator implements LineGeneratorStrategy {

    @Value("${limit.bound.upper}")
    private int randomNumberUpperBound;

    @Value("${limit.bound.lower}")
    private int randomNumberLowerBound;

    private Random random = new Random();

    private int generateNumber() {
        return random.ints(randomNumberLowerBound, randomNumberUpperBound)
                .findFirst()
                .getAsInt();
    }

    @Override
    public TicketLine generateLine() {
        return new TicketLine(generateNumber(), generateNumber(), generateNumber());
    }
}