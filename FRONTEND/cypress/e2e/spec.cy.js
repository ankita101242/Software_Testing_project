describe('template spec', () => {
  it('passes', () => {
    cy.visit('https://example.cypress.io');
  });
});

describe('Navigation Test for Login Page', () => {
  it('Loads the Login Page', () => {
    cy.visit('/');
    cy.url().should('include', '/login');
    cy.title().should('eq', 'Prose Petal');
  }); 

}); 


// describe('Session Data Recording Tests', () => {
//   beforeEach(() => {
//     cy.intercept('POST', '/api/session/logs', {
//       statusCode: 200,
//     }).as('logSession');
//   });

//   it('should send session data to the server on page load', () => {
//     cy.visit('/homepage');

//     // Check if the session data logging API is called
//     cy.wait('@logSession').then((interception) => {
//       expect(interception.request.body).to.include.keys('sessionId', 'timestamp', 'userAgent');
//     });
//   });
// });

