package com.mercan.lottery.repository;

import com.mercan.lottery.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
