package com.mercan.lottery.controller;

import com.mercan.lottery.dto.ApiError;
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
import java.util.List;


@RestController
@RequestMapping(value = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Get lottery ticket for given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All tickets are retrieved", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable(value = "ticketId")
                                            @Parameter(description = "Ticket id to be updated ", name = "ticketId")
                                                    Long ticketId) {
        log.info("get ticket ticketId: {}", ticketId);
        Ticket ticket = ticketService.getTicket(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }


    @Operation(summary = "Get all lottery tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All tickets are retrieved", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})

    @GetMapping()
    public ResponseEntity<List<Ticket>> getAllTickets() {
        log.info("get lottery tickets");
        List<Ticket> ticketList = ticketService.getAllTickets();
        return ResponseEntity.status(HttpStatus.OK).body(ticketList);
    }

    @Operation(summary = "Create lottery ticket with given number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket is created", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "400", description = "Missing or incorrect parameter check response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
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

    @PutMapping("/{ticketId}")
    @Operation(summary = "Update lottery ticket with given number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket is updated", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "404", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Ticket is checked before", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "400", description = "Missing or incorrect parameter check response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))})})
    public ResponseEntity<Ticket> updateTicket(@RequestParam(value = "numberOfLines")
                                               @Parameter(description = "Number of lines to be added to the stored ticket", name = "numberOfLines")
                                               @Min(message = "numberOfLines can not be lower than 1.", value = 0)
                                                       Integer numberOfLines,

                                               @PathVariable(value = "ticketId")
                                               @Parameter(description = "Ticket id to be updated ", name = "ticketId")
                                                       Long ticketId) {
        log.info("update lottery ticketId: {} line numbers :{}", ticketId, numberOfLines);
        Ticket ticket = ticketService.updateTicket(ticketId, numberOfLines);
        log.info("update lottery ticketId :{} result:{}", ticketId, ticket);

        return ResponseEntity.ok(ticket);
    }


    @DeleteMapping("/{ticketId}")
    @Operation(summary = "Update lottery ticket with given number of lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket is deleted", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Ticket is not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))}),

    })
    public ResponseEntity<Void> delete(@PathVariable(value = "ticketId")
                                       @Parameter(description = "Ticket id to be updated ", name = "ticketId")
                                               Long ticketId) {
        log.info("delete lottery ticketId: {}", ticketId);
        ticketService.delete(ticketId);

        return ResponseEntity.noContent().build();
    }
}