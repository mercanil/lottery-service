package com.mercan.lottery.bdd.commons;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercan.lottery.dto.ApiError;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TicketHttpClient {

    private final String SERVER_URL = "http://localhost";
    private final String TICKET_BASE_PATH = "/ticket";


    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplateBuilder().errorHandler(new DefaultResponseErrorHandler()).build();


    private String lotteryEndpoint() {
        return SERVER_URL + ":" + port + TICKET_BASE_PATH;
    }


    public void post(final Integer numberOfLines) throws JsonProcessingException {
        try {
            ResponseEntity ticketResponseEntity = restTemplate.postForEntity(lotteryEndpoint() + "?numberOfLines={numberOfLines}", null, Ticket.class, numberOfLines);
            RequestContext.storeTicketResponse(ticketResponseEntity);
        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }
    }

    public void put(final Integer numberOfLines, final Long ticketId) throws JsonProcessingException {
        try {
            restTemplate.put(lotteryEndpoint() + "/" + ticketId + "?numberOfLines={numberOfLines}", null, numberOfLines);
        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }
    }

    public void getAllTickets() throws JsonProcessingException {
        try {
            ResponseEntity<List> ticketResultResponseEntity = restTemplate.getForEntity(lotteryEndpoint(), List.class);
            RequestContext.storeTicketListResult(ticketResultResponseEntity);
        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }
    }

    public void getTicket(long ticketId) throws JsonProcessingException {
        try {
            ResponseEntity<Ticket> ticketResponseEntity = restTemplate.getForEntity(lotteryEndpoint() + "/" + ticketId, Ticket.class);
            RequestContext.storeTicketResponse(ticketResponseEntity);
        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }
    }

    public void delete(long ticketId) throws JsonProcessingException {
        try {
            restTemplate.delete(lotteryEndpoint() + "/" + ticketId);
        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }

    }
}
