package com.mercan.lottery.repository;

import com.mercan.lottery.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    @Override
    List<Ticket> findAll();
}
