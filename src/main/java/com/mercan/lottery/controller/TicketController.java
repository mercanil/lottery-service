package com.mercan.lottery.controller;

import com.mercan.lottery.dto.ApiError;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;


@RestController
@RequestMapping(value = "/api/lottery/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class TicketController {

    private final TicketService ticketService;


    @Operation(summary = "Check lottery ticket by given id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of ticket and also sets status of ticket as checked", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TicketResult.class))}),
            @ApiResponse(responseCode = "400", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Missing or incorrect parameter check response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    @GetMapping(value = "/check")
    public ResponseEntity<TicketResult> checkTicket(@RequestParam(value = "ticketId")
                                                    @Parameter(description = "Ticket id to be checked", name = "ticketId")
                                                            Long ticketId) {
        log.info("check lottery result for id :{}", ticketId);
        TicketResult ticketResult = ticketService.checkTicket(ticketId);
        log.info("check lottery result for id :{} , response:{}", ticketId, ticketResult);

        return ResponseEntity.ok(ticketResult);
    }


    @Operation(summary = "Create lottery ticket with given number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket is created", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "404", description = "Missing or incorrect parameter check response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    @PostMapping()
    public ResponseEntity<Ticket> createTicket(@RequestParam(value = "numberOfLines")
                                               @Parameter(description = "Number of lines to be added to the stored ticket", name = "numberOfLines")
                                               @Min(message = "numberOfLines can not be lower than 1.", value = 0)
                                                       Integer numberOfLines) {
        log.info("create lottery line numbers :{}", numberOfLines);
        Ticket ticket = ticketService.generateTicket(numberOfLines);
        log.info("create lottery line numbers :{} result:{}", numberOfLines, ticket);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }


    @PutMapping()
    @Operation(summary = "Update lottery ticket with given number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket is updated", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "400", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "400", description = "Ticket is checked before", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Missing or incorrect parameter check response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    public ResponseEntity<Ticket> updateTicket(@RequestParam(value = "numberOfLines")
                                               @Parameter(description = "Number of lines to be added to the stored ticket", name = "numberOfLines")
                                               @Min(message = "numberOfLines can not be lower than 1.", value = 0)
                                                       Integer numberOfLines,

                                               @RequestParam(value = "ticketId")
                                               @Parameter(description = "Ticket id to be updated ", name = "ticketId")
                                                       Long ticketId) {
        log.info("update lottery ticketId: {} line numbers :{}", ticketId, numberOfLines);
        Ticket ticket = ticketService.updateTicket(ticketId, numberOfLines);
        log.info("update lottery lticketId :{} result:{}", ticketId, ticket);

        return ResponseEntity.ok(ticket);
    }
}