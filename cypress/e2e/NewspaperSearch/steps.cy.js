import {Given, When, Then, Before} from "@badeball/cypress-cucumber-preprocessor"
import {NewsPaperSearch} from "./newsPaperSearch.cy";

Given(/^: I open Google archive$/, function () {
    cy.visit("https://news.google.com/newspapers")
});
Given(/^: click on 'The Age' newspaper$/, function () {
    cy.xpath('//b[text() = \'The Age\']').click()
});
When(/^: I search by year when newspaper was printed$/, function () {
    const newsPaperSearch = new NewsPaperSearch();
    newsPaperSearch.search('1980')
});

When(/^: I search by year & month when newspaper was printed$/, function () {
    const newsPaperSearch = new NewsPaperSearch();
    newsPaperSearch.search('FEB 1980')
});

When(/^: I search by year in the future$/, function () {
    const newsPaperSearch = new NewsPaperSearch();
    newsPaperSearch.search('2023')
});

Then(/^: I see scans of the newspaper$/, function () {
    //assertion
    cy.get('.newspsummary').should("be.visible")
});
Then(/^: I don't see any scans$/, function () {
    //assertion
    cy.get('.newspsummary').should("not.exist")
});