Feature: Lottery Features

  Scenario: Create Ticket
    When  I want to create a ticket with 2 lines
    Then  Ticket should be stored with 2 lines


  Scenario: Create Ticket without numberOfLines
    When  I want to create this ticket but forget to add numberOfLines
    Then  I should receive an error contains "Required Integer parameter 'numberOfLines' is not present"

  Scenario: Create Ticket with invalid numberOfLines
    When  I want to create a ticket with -2 lines
    Then  I should receive an error contains "numberOfLines can not be lower than 1. Input value:{-2}"


  Scenario: Update ticket for an existing ticket
    Given I have a ticket with 2 lines
    When  I want to add new 3 lines to this ticket
    Then  I should receive updated ticket with 5 lines


  Scenario: Update ticket without numberOfLines
    Given I have a ticket with 2 lines
    When  I want to add new lines to this ticket but forget to add numberOfLines
    Then  I should receive an error contains "Required Integer parameter 'numberOfLines' is not present"


  Scenario: Update ticket with invalid numberOfLines
    Given I have a ticket with 2 lines
    When  I want to add new lines to this ticket but provide invalid -1 value to numberOfLines
    Then  I should receive an error contains "numberOfLines can not be lower than 1. Input value:{-1}"

  Scenario: Update ticket without ticketId
    Given I have a ticket with 2 lines
    When  I want to add new 3 lines to this ticket but forget to add ticketId
    Then  I should receive an error contains "Required Long parameter 'ticketId' is not present"

  Scenario: Update ticket with invalid ticketId
    Given I have a ticket with 2 lines
    When  I want to add new 3 lines to this ticket but provide invalid -1 ticketId
    Then  I should receive an error contains "ticket is not found for id -1"


  Scenario: Update ticket which is checked before
    Given I have a ticket with 2 lines
    But   This ticket is checked before
    When  I want to add new 3 lines to this ticket
    Then  I should receive an error contains "has been checked before."


  Scenario: Check ticket
    Given I have a ticket with 2 lines
    When  I want to check ticket
    Then  I should receive ticket with 2 lines contains results


  Scenario: Check ticket without ticketId
    Given I have a ticket with 2 lines
    When  I want to check ticket but forget to provide ticketId
    Then  I should receive an error contains "Required Long parameter 'ticketId' is not present"

  Scenario: Check non existing ticket
    Given I have a ticket with 2 lines
    When  I want to check ticket but provide invalid ticketId -1
    Then  I should receive an error contains "ticket is not found for id -1"