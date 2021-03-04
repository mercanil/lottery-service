package com.mercan.lottery.service;

import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mercan.lottery.TestHelper.createTicket;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    TicketRepository mockTicketRepository;
    @Mock
    LineResultCalculatorStrategy lineResultCalculatorStrategy;

    @InjectMocks
    StatusService classUnderTest;

    @Test
    void check_ticket_expect_success() {

        //given
        int numberOfLines = 1;
        Ticket storedTicket = createTicket(numberOfLines);
        given(mockTicketRepository.findById(storedTicket.getId())).willReturn(Optional.of(storedTicket));
        given(mockTicketRepository.save(storedTicket)).willReturn(storedTicket);
        given(lineResultCalculatorStrategy.calculateResult(storedTicket.getTicketLines().get(0))).willReturn(12);

        //when
        TicketResult ticketResult = classUnderTest.checkStatus(storedTicket.getId());

        //then
        assertThat(ticketResult.isChecked(), is(true));
        assertThat(ticketResult.getResults(), hasSize(numberOfLines));
        verify(mockTicketRepository).findById(storedTicket.getId());

    }


    @Test
    void check_ticket_expect_exception_when_ticket_is_not_found() {

        //given
        long invalidId = 1;
        given(mockTicketRepository.findById(invalidId)).willReturn(Optional.empty());


        //when then
        Assertions.assertThrows(TicketNotFoundException.class, () -> classUnderTest.checkStatus(invalidId));
        verify(mockTicketRepository).findById(invalidId);
        verify(mockTicketRepository, never()).save(any());
    }

}