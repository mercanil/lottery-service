package com.mercan.lottery.service.strategy;

import com.mercan.lottery.entity.TicketLine;

public interface LineGeneratorStrategy {
    TicketLine generateLine();
}
