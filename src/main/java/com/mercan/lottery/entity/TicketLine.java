package com.mercan.lottery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int[] numbers = new int[3];

    @Version
    private long version;

    public TicketLine(int number1, int number2, int number3) {
        numbers[0] = number1;
        numbers[1] = number2;
        numbers[2] = number3;
    }
}

