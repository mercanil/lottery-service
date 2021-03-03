package com.mercan.lottery.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mercan.lottery.entity.TicketLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineResult {

    private TicketLine line;
    private Integer result;

    public LineResult(TicketLine line, int result) {
        this.line = line;
        this.result = result;
    }
}