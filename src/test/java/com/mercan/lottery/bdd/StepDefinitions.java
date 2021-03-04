package com.mercan.lottery.bdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercan.lottery.bdd.commons.HttpClient;
import com.mercan.lottery.bdd.commons.RequestContext;
import com.mercan.lottery.dto.ApiError;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.repository.TicketRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    TicketRepository repository;


    @After
    public void afterStep() {
        repository.deleteAll();
    }

    @Given("I have a ticket with {int} lines")
    public void iHaveATicketWithLines(final int numberOfLines) throws JsonProcessingException {
        httpClient.post(numberOfLines);
    }

    @Given("I have {int} tickets with {int} lines each")
    public void iHaveATicketWithLines(final int ticketCount, final int numberOfLines) throws JsonProcessingException {
        for (int i = 0; i < ticketCount; i++) {
            iHaveATicketWithLines(numberOfLines);
        }
    }

    @When("I want retrieve all tickets")
    public void iWantRetrieveAllTickets() throws JsonProcessingException {
        httpClient.getAllTickets();
    }


    @Then("I should receive all tickets")
    public void iShouldReceiveAllTickets() {
        ResponseEntity<List<Ticket>> ticketResponse = RequestContext.getTicketListResult();
        List<Ticket> storedTicketList = repository.findAll();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResponse.getBody().size(), is(storedTicketList.size()));
    }

    @Then("I should receive empty response")
    public void iShouldReceiveEmptyResponse() {
        ResponseEntity<List<Ticket>> ticketResponse = RequestContext.getTicketListResult();
        List<Ticket> storedTicketList = repository.findAll();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResponse.getBody().size(), is(storedTicketList.size()));
        assertThat(ticketResponse.getBody(), empty());
        assertThat(storedTicketList, empty());
    }


    @When("I want to create a ticket with {int} lines")
    public void iWantToCreateATicketWithLines(final int numberOfLines) throws JsonProcessingException {
        httpClient.post(numberOfLines);
    }

    @When("I want to create this ticket but forget to add numberOfLines")
    public void iWantToCreateThisTicketButForgetToAddNumberOfLines() throws JsonProcessingException {
        httpClient.post(null);
    }


    @Then("Ticket should be stored with {int} lines")
    public void ticketShouldBeStoredWithLines(final int numberOfLines) {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        Ticket storedTicket = repository.findById(RequestContext.getTicketResponse().getBody().getId()).get();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.CREATED.value()));
        assertThat(ticketResponse.getBody(), is(storedTicket));
        assertThat(storedTicket.isChecked(), is(false));
        assertThat(storedTicket.getTicketLines(), hasSize(numberOfLines));
        assertThat(storedTicket.getId(), is(notNullValue()));
    }

    @Then("I should receive an error contains {string}")
    public void iShouldReceiveAnErrorContains(String error) {
        ResponseEntity<ApiError> ticketResponse = RequestContext.getTicketError();
        ApiError apiError = ticketResponse.getBody();
        assertThat(apiError.getErrors().get(0), containsString(error));
        assertTrue(ticketResponse.getStatusCode().isError());
    }

    @When("I want to add new {int} lines to this ticket")
    public void iWantToAddnewLinesToThisTicket(int additionalLines) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.put(additionalLines, ticketResponse.getBody().getId());
    }

    @Then("I should receive updated ticket with {int} lines")
    public void iShouldReceiveUpdateTicketWithLines(int totalLines) {
        Ticket storedTicket = repository.findById(RequestContext.getTicketResponse().getBody().getId()).get();
        assertThat(storedTicket.isChecked(), is(false));
        assertThat(storedTicket.getTicketLines(), hasSize(totalLines));
        assertThat(storedTicket.getId(), is(notNullValue()));
    }


    @When("I want to add new lines to this ticket but forget to add numberOfLines")
    public void iWantToAddNewLinesToThisTicketButForgetToAddNumberOfLines() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.put(null, ticketResponse.getBody().getId());
    }

    @When("I want to add new lines to this ticket but provide invalid {int} value to numberOfLines")
    public void iWantToAddNewLinesToThisTicketButProvideInvalidValue(int additionalLines) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.put(additionalLines, ticketResponse.getBody().getId());
    }

    @When("I want to add new {int} lines to this ticket but forget to add ticketId")
    public void iWantToAddNewLinesToThisTicketButForgetToAddTicketId(int additionalLines) throws JsonProcessingException {
        httpClient.put(additionalLines, null);
    }

    @When("I want to add new {int} lines to this ticket but provide invalid {long} ticketId")
    public void iWantToAddNewLinesToThisTicketButProvideInvalidTicketId(int additionalLines, long ticketId) throws JsonProcessingException {
        httpClient.put(additionalLines, ticketId);
    }

    @But("This ticket is checked before")
    public void thisTicketIsCheckedBefore() throws JsonProcessingException {
        iWantToCheckTicket();
    }

    @When("I want to check ticket")
    public void iWantToCheckTicket() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.get(ticketResponse.getBody().getId());
    }

    @Then("I should receive ticket with {int} lines contains results")
    public void iShouldReceiveTicketWithLinesContainsResult(int totalLines) {
        ResponseEntity<TicketResult> ticketResultResponse = RequestContext.getTicketResultResponse();
        TicketResult ticketResult = ticketResultResponse.getBody();
        assertThat(ticketResultResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResult.getTicket().isChecked(), is(true));

        assertThat(ticketResult.getResults(), hasSize(totalLines));
        assertThat(ticketResult.getTicket().getId(), is(notNullValue()));

    }

    @When("I want to check ticket but forget to provide ticketId")
    public void iWantToCheckTicketButForgetToProvideTicketId() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.get(null);
    }

    @When("I want to check ticket but provide invalid ticketId {long}")
    public void iWantToCheckTicketButForgetToProvideTicketId(final long ticketId) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        httpClient.get(ticketId);
    }

}
