package com.mercan.lottery.service;

import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.entity.TicketFactory;
import com.mercan.lottery.exception.TicketCheckedException;
import com.mercan.lottery.exception.TicketNotFoundException;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.repository.TicketRepository;
import com.mercan.lottery.service.strategy.LineGeneratorStrategy;
import com.mercan.lottery.service.strategy.LineResultCalculatorStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mercan.lottery.TestHelper.createTicket;
import static com.mercan.lottery.TestHelper.createTicketLine;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    TicketRepository mockTicketRepository;

    @Mock
    TicketFactory mockTicketFactory;

    @Mock
    LineGeneratorStrategy mockLineGenerator;


    @Mock
    LineResultCalculatorStrategy lineResultCalculatorStrategy;

    @InjectMocks
    TicketService classUnderTest;


    @Test
    void generate_ticket_expect_success() {

        //given
        int numberOfLines = 1;
        Ticket createdTicket = createTicket(numberOfLines);
        given(mockTicketFactory.generateTicket(numberOfLines)).willReturn(createdTicket);
        given(mockTicketRepository.save(createdTicket)).willReturn(createdTicket);

        //when
        Ticket ticket = classUnderTest.generateTicket(numberOfLines);

        //then
        assertThat(ticket.isChecked(), is(false));
        assertThat(ticket.getTicketLines(), hasSize(numberOfLines));
        verify(mockTicketFactory, times(1)).generateTicket(numberOfLines);

    }

    @Test
    void update_ticket_expect_success() {

        //given
        int numberOfLines = 1;
        int additionalLine = 1;
        Ticket storedTicket = createTicket(numberOfLines);
        given(mockTicketRepository.findById(storedTicket.getId())).willReturn(Optional.of(storedTicket));
        given(mockLineGenerator.generateLine()).willReturn(createTicketLine());
        given(mockTicketRepository.save(storedTicket)).willReturn(storedTicket);

        //when
        Ticket ticket = classUnderTest.updateTicket(storedTicket.getId(), additionalLine);

        //then
        assertThat(ticket.isChecked(), is(false));
        assertThat(ticket.getTicketLines(), hasSize(numberOfLines + additionalLine));
        verify(mockTicketRepository).findById(storedTicket.getId());

    }

    @Test
    void update_ticket_expect_exception_when_ticket_is_not_found() {

        //given
        int numberOfLines = 1;
        int additionalLine = 1;
        long invalidId = 1;
        Ticket storedTicket = createTicket(numberOfLines);
        given(mockTicketRepository.findById(invalidId)).willReturn(Optional.empty());


        //when then
        long storedTicketId = storedTicket.getId();
        Assertions.assertThrows(TicketNotFoundException.class, () -> classUnderTest.updateTicket(storedTicketId, additionalLine));

        verify(mockTicketRepository).findById(invalidId);
        verify(mockTicketRepository, never()).save(any());
        verify(mockLineGenerator, never()).generateLine();

    }


    @Test
    void update_ticket_expect_exception_when_ticket_is_already_checked() {

        //given
        int numberOfLines = 1;
        int additionalLine = 1;
        long invalidId = 1;
        Ticket storedTicket = createTicket(numberOfLines);
        storedTicket.setChecked(true);
        given(mockTicketRepository.findById(storedTicket.getId())).willReturn(Optional.of(storedTicket));


        // when then
        long storedTicketId = storedTicket.getId();
        Assertions.assertThrows(TicketCheckedException.class, () -> classUnderTest.updateTicket(storedTicketId, additionalLine));
        verify(mockTicketRepository).findById(invalidId);
        verify(mockTicketRepository, never()).save(any());
        verify(mockLineGenerator, never()).generateLine();

    }


    @Test
    void check_ticket_expect_success() {

        //given
        int numberOfLines = 1;
        Ticket storedTicket = createTicket(numberOfLines);
        given(mockTicketRepository.findById(storedTicket.getId())).willReturn(Optional.of(storedTicket));
        given(mockTicketRepository.save(storedTicket)).willReturn(storedTicket);
        given(lineResultCalculatorStrategy.calculateResult(storedTicket.getTicketLines().get(0))).willReturn(12);

        //when
        TicketResult ticketResult = classUnderTest.checkTicket(storedTicket.getId());

        //then
        assertThat(ticketResult.getTicket().isChecked(), is(true));
        assertThat(ticketResult.getTicket().getTicketLines(), hasSize(numberOfLines));
        verify(mockTicketRepository).findById(storedTicket.getId());

    }


    @Test
    void check_ticket_expect_exception_when_ticket_is_not_found() {

        //given
        long invalidId = 1;
        given(mockTicketRepository.findById(invalidId)).willReturn(Optional.empty());


        //then
        Assertions.assertThrows(TicketNotFoundException.class, () -> classUnderTest.checkTicket(invalidId));
        verify(mockTicketRepository).findById(invalidId);
        verify(mockTicketRepository, never()).save(any());
        verify(mockLineGenerator, never()).generateLine();

    }


}