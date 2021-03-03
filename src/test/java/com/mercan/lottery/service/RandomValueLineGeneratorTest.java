package com.mercan.lottery.service;

import com.mercan.lottery.entity.TicketLine;
import com.mercan.lottery.service.strategy.RandomValueLineGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.fail;

class RandomValueLineGeneratorTest {

    private RandomValueLineGenerator randomValueLineGenerator = new RandomValueLineGenerator();


    @Test
    void test_generated_nubmers() {
        //given
        int upperBound = 3;
        int lowerBound = 0;
        ReflectionTestUtils.setField(randomValueLineGenerator, "randomNumberUpperBound", upperBound);
        ReflectionTestUtils.setField(randomValueLineGenerator, "randomNumberLowerBound", lowerBound);

        for (int i = 0; i < 100000; i++) {
            TicketLine line = randomValueLineGenerator.generateLine();
            int[] numbers = line.getNumbers();
            assertNumberBounds(numbers[0], upperBound, lowerBound);
            assertNumberBounds(numbers[1], upperBound, lowerBound);
            assertNumberBounds(numbers[2], upperBound, lowerBound);

        }
    }

    private void assertNumberBounds(int number, int upperBound, int lowerBound) {
        if (number >= upperBound || number < lowerBound) {
            fail("generated values are not in the permitted boundaries");
        }
    }
}