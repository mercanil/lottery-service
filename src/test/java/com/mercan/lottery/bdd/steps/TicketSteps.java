package com.mercan.lottery.bdd.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercan.lottery.bdd.commons.RequestContext;
import com.mercan.lottery.bdd.commons.TicketHttpClient;
import com.mercan.lottery.dto.ApiError;
import com.mercan.lottery.entity.Ticket;
import com.mercan.lottery.repository.TicketRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketSteps {

    @Autowired
    protected TicketRepository ticketRepository;
    @Autowired
    protected TicketHttpClient ticketHttpClient;


    @After
    public void afterStep() {
        ticketRepository.deleteAll();
    }

    @When("I want retrieve all tickets")
    public void iWantRetrieveAllTickets() throws JsonProcessingException {
        ticketHttpClient.getAll();
    }

    @When("I want retrieve this ticket")
    public void iWantToRetrieveThisTicket() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        ticketHttpClient.get(ticketResponse.getBody().getId());
    }

    @When("I want retrieve this ticket with invalid id {long}")
    public void iWantRetrieveThisTicketWithInvalidId(final long invalidTicketId) throws JsonProcessingException {
        ticketHttpClient.get(invalidTicketId);
    }

    @Then("I should receive this ticket successfully")
    public void iShouldReceiveThisTicketSuccessfully() {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        Ticket storedTicket = ticketRepository.findById(RequestContext.getTicketResponse().getBody().getId()).get();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(storedTicket.isChecked(), is(ticketResponse.getBody().isChecked()));
        assertThat(storedTicket.getTicketLines().size(), is(ticketResponse.getBody().getTicketLines().size()));
        assertThat(storedTicket.getId(), is(ticketResponse.getBody().getId()));
    }

    @Then("I should receive all tickets")
    public void iShouldReceiveAllTickets() {
        ResponseEntity<List<Ticket>> ticketResponse = RequestContext.getTicketListResult();
        List<Ticket> storedTicketList = ticketRepository.findAll();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResponse.getBody().size(), is(storedTicketList.size()));
    }

    @When("I want to create a ticket with {int} lines")
    public void iWantToCreateATicketWithLines(final int numberOfLines) throws JsonProcessingException {
        ticketHttpClient.post(numberOfLines);
    }

    @When("I want to create this ticket but forget to add numberOfLines")
    public void iWantToCreateThisTicketButForgetToAddNumberOfLines() throws JsonProcessingException {
        ticketHttpClient.post(null);
    }

    @Then("Ticket should be stored with {int} lines")
    public void ticketShouldBeStoredWithLines(final int numberOfLines) {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        Ticket storedTicket = ticketRepository.findById(RequestContext.getTicketResponse().getBody().getId()).get();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.CREATED.value()));
        assertThat(ticketResponse.getBody(), is(storedTicket));
        assertThat(storedTicket.isChecked(), is(false));
        assertThat(storedTicket.getTicketLines(), hasSize(numberOfLines));
        assertThat(storedTicket.getId(), is(notNullValue()));
    }

    @When("I want to add new {int} lines to this ticket")
    public void iWantToAddnewLinesToThisTicket(int additionalLines) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        ticketHttpClient.put(additionalLines, ticketResponse.getBody().getId());
    }

    @When("I want to add new lines to this ticket but forget to add numberOfLines")
    public void iWantToAddNewLinesToThisTicketButForgetToAddNumberOfLines() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        ticketHttpClient.put(null, ticketResponse.getBody().getId());
    }

    @When("I want to add new lines to this ticket but provide invalid {int} value to numberOfLines")
    public void iWantToAddNewLinesToThisTicketButProvideInvalidValue(int additionalLines) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        ticketHttpClient.put(additionalLines, ticketResponse.getBody().getId());
    }

    @When("I want to add new {int} lines to this ticket but forget to add ticketId")
    public void iWantToAddNewLinesToThisTicketButForgetToAddTicketId(int additionalLines) throws JsonProcessingException {
        ticketHttpClient.put(additionalLines, null);
    }

    @When("I want to add new {int} lines to this ticket but provide invalid {long} ticketId")
    public void iWantToAddNewLinesToThisTicketButProvideInvalidTicketId(int additionalLines, long ticketId) throws JsonProcessingException {
        ticketHttpClient.put(additionalLines, ticketId);
    }

    @Then("I should receive updated ticket with {int} lines")
    public void iShouldReceiveUpdatedTicketWithLines(int totalLines) {
        Ticket storedTicket = ticketRepository.findById(RequestContext.getTicketResponse().getBody().getId()).get();
        assertThat(storedTicket.isChecked(), is(false));
        assertThat(storedTicket.getTicketLines(), hasSize(totalLines));
        assertThat(storedTicket.getId(), is(notNullValue()));
    }

    @Given("I have a ticket with {int} lines")
    public void iHaveATicketWithLines(final int numberOfLines) throws JsonProcessingException {
        ticketHttpClient.post(numberOfLines);
    }

    @Given("I have {int} tickets with {int} lines")
    public void iHaveMultipleTicketsWithLines(final int ticketCount, final int numberOfLines) throws JsonProcessingException {
        for (int i = 0; i < ticketCount; i++) {
            ticketHttpClient.post(numberOfLines);
        }
    }

    @When("I want to delete this ticket")
    public void iWantToDeleteThisTicket() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();

        ticketHttpClient.delete(ticketResponse.getBody().getId());
    }

    @Then("Ticket should be deleted")
    public void ticketShouldBeDeleted() {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        Optional<Ticket> ticket = ticketRepository.findById(ticketResponse.getBody().getId());
        assertThat(ticket.isPresent(), is(false));
    }

    @When("I want to delete ticket by invalid {long} id")
    public void iWantToDeleteTicketByInvalidId(final long ticketId) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        ticketHttpClient.delete(ticketId);
    }


    @Given("I have no tickets")
    public void iHaveNoTickets() {
        //DO NOTHING
    }

    @Then("I should receive empty response")
    public void iShouldReceiveEmptyResponse() {
        ResponseEntity<List<Ticket>> ticketResponse = RequestContext.getTicketListResult();
        List<Ticket> storedTicketList = ticketRepository.findAll();

        assertThat(ticketResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResponse.getBody().size(), is(storedTicketList.size()));
        assertThat(ticketResponse.getBody(), empty());
        assertThat(storedTicketList, empty());
    }

    @Then("I should receive an error contains {string}")
    public void iShouldReceiveAnErrorContains(String error) {
        ResponseEntity<ApiError> ticketResponse = RequestContext.getTicketError();
        ApiError apiError = ticketResponse.getBody();
        assertThat(apiError.getErrors().get(0), containsString(error));
        assertTrue(ticketResponse.getStatusCode().isError());
    }
}
