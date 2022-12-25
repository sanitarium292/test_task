export class NewsPaperSearch{

    search = (date) => {
        cy.get('.datefield').type(date)
        //request
        cy.intercept('GET', '**/newspapers**').as('getScans')
        cy.get('.jumpbutton').click()
        //wait for the request
        cy.wait('@getScans')
    }
}