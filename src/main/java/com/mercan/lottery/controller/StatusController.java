package com.mercan.lottery.controller;

import com.mercan.lottery.dto.ApiError;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatusController {


    private final StatusService statusService;

    @Operation(summary = "Check status of lottery ticket by given id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of ticket and also sets status of ticket as checked", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TicketResult.class))}),
            @ApiResponse(responseCode = "404", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    @PutMapping(value = "/{ticketId}")
    public ResponseEntity<TicketResult> checkStatus(@PathVariable(value = "ticketId")
                                                    @Parameter(description = "Ticket id to be checked", name = "ticketId")
                                                            Long ticketId) {
        log.info("check lottery result for id :{}", ticketId);
        TicketResult ticketResult = statusService.checkStatus(ticketId);
        log.info("check lottery result for id :{} , response:{}", ticketId, ticketResult);

        return ResponseEntity.ok(ticketResult);
    }
}
