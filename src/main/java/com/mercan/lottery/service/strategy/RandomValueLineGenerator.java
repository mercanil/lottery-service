package com.mercan.lottery.service.strategy;

import com.mercan.lottery.entity.TicketLine;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomValueLineGenerator implements LineGeneratorStrategy {

    private SecureRandom random = new SecureRandom();

    private int generateNumber() {
        return random.ints(0, 3)
                .findFirst()
                .getAsInt();
    }

    @Override
    public TicketLine generateLine() {
        return new TicketLine(generateNumber(), generateNumber(), generateNumber());
    }
}