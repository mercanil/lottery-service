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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class StatusHttpClient {

    @Autowired
    ObjectMapper objectMapper;

    private final String SERVER_URL = "http://localhost";
    private final String STATUS_ENDPOINT = "/status";

    @LocalServerPort
    private int port;
    private final RestTemplate restTemplate = new RestTemplateBuilder().errorHandler(new DefaultResponseErrorHandler()).build();


    private String statusEndPoint() {
        return SERVER_URL + ":" + port + STATUS_ENDPOINT;
    }

    public void put(final Long ticketId) throws JsonProcessingException {
        try {
            ResponseEntity<TicketResult> ticketResult = restTemplate.exchange(statusEndPoint() + "/" + ticketId, HttpMethod.PUT, null, TicketResult.class);
            RequestContext.storeTicketResultResponse(ticketResult);

        } catch (final HttpClientErrorException e) {
            ApiError apiError = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
            RequestContext.storeErrorResponse(new ResponseEntity<>(apiError, e.getStatusCode()));
        }
    }

}
