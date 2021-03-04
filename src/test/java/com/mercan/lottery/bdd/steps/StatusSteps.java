package com.mercan.lottery.bdd.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercan.lottery.bdd.commons.RequestContext;
import com.mercan.lottery.bdd.commons.StatusHttpClient;
import com.mercan.lottery.dto.TicketResult;
import com.mercan.lottery.entity.Ticket;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StatusSteps {


    @Autowired
    protected StatusHttpClient statusHttpClient;

    @But("This ticket is checked before")
    public void thisTicketIsCheckedBefore() throws JsonProcessingException {
        iWantToCheckTicket();
    }

    @When("I want to check ticket")
    public void iWantToCheckTicket() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        statusHttpClient.put(ticketResponse.getBody().getId());
    }

    @Then("I should receive ticket with {int} lines contains results")
    public void iShouldReceiveTicketWithLinesContainsResult(int totalLines) {
        ResponseEntity<TicketResult> ticketResultResponse =  RequestContext.getTicketResultResponse();
        TicketResult ticketResult = ticketResultResponse.getBody();
        assertThat(ticketResultResponse.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(ticketResult.getTicket().isChecked(), is(true));

        assertThat(ticketResult.getResults(), hasSize(totalLines));
        assertThat(ticketResult.getTicket().getId(), is(notNullValue()));

    }

    @When("I want to check ticket but forget to provide ticketId")
    public void iWantToCheckTicketButForgetToProvideTicketId() throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        statusHttpClient.put(null);
    }

    @When("I want to check ticket but provide invalid ticketId {long}")
    public void iWantToCheckTicketButForgetToProvideTicketId(final long ticketId) throws JsonProcessingException {
        ResponseEntity<Ticket> ticketResponse = RequestContext.getTicketResponse();
        statusHttpClient.put(ticketId);
    }
}
