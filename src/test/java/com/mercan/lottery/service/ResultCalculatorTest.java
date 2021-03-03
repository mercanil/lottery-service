package com.mercan.lottery.service;

import com.mercan.lottery.entity.TicketLine;
import com.mercan.lottery.service.strategy.ResultCalculator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ResultCalculatorTest {

    private ResultCalculator resultCalculator = new ResultCalculator();


    @Test
     void test_result_of_numbers_when_sum_is_two() {
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 1, 1)), is(10));
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 0, 1)), is(10));
        assertThat(resultCalculator.calculateResult(new TicketLine(2, 0, 0)), is(10));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 0, 2)), is(10));
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 0, 1)), is(10));

    }

    @Test
     void test_result_of_numbers_when_all_values_are_same() {
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 1, 1)), is(5));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 0, 0)), is(5));
        assertThat(resultCalculator.calculateResult(new TicketLine(2, 2, 2)), is(5));

    }

    @Test
     void test_result_of_numbers_when_2_and_3_are_different_from_1() {
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 0, 2)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 2, 0)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 2, 2)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(1, 0, 0)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 2, 1)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 1, 2)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 2, 2)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(2, 0, 1)), is(1));
        assertThat(resultCalculator.calculateResult(new TicketLine(2, 1, 1)), is(1));

    }

    @Test
     void test_result_of_numbers_when_2_and_3_are_different_from_1_but_sum_is_2() {
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 1, 1)), is(10));
        assertThat(resultCalculator.calculateResult(new TicketLine(0, 2, 0)), is(10));

    }

}