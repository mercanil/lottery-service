package com.mercan.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

    private String reasonCode;
    private List<String> errors;
}
