describe('Login and Session Management Test', () => {
  const loginUrl = 'http://localhost:9595/api/v1/auth/authenticate'; // API endpoint for login
  const homepageUrl = 'http://localhost:3000/homepage'; // Homepage URL after login
  const loginPageUrl = 'http://localhost:3000/login'; // Login page URL

  beforeEach(() => {
    cy.visit('/login'); // Ensure the test starts at the login page
  });

  it('Logs in and persists session correctly', () => {
    // Intercept the login request to mock a successful response
    cy.intercept('POST', loginUrl, {
      statusCode: 200,
      body: {
        token: 'mock-auth-token',
        user_id: '12345'
      }
    }).as('loginRequest');

    // Fill in the login form
    cy.get('#email').type('testuser@example.com');
    cy.get('#password').type('password123');
    cy.get('button[type="submit"]').click();

    // Wait for the login request to be processed
    cy.wait('@loginRequest');

    // Verify that the token and user_id are stored in local storage
    cy.window().then((window) => {
      expect(window.localStorage.getItem('authToken')).to.equal('mock-auth-token');
    });

    // Verify redirection to the homepage
    cy.url().should('eq', homepageUrl);

    // Check if the Navbar contains the logout button
    cy.get('.navbar-dark-gray').within(() => {
      cy.contains('LOGOUT').should('exist');
    });
  });

  it('Logs out and clears the session', () => {
    // Simulate logged-in state by setting local storage directly
    cy.window().then((window) => {
      window.localStorage.setItem('authToken', 'mock-auth-token');
    });

    // Visit the homepage
    cy.visit('/homepage');

    // Verify the logout button is visible
    cy.get('.navbar-dark-gray').within(() => {
      cy.contains('LOGOUT').click();
    });

    // Verify session data is cleared
    cy.window().then((window) => {
      expect(window.localStorage.getItem('authToken')).to.be.null;
    });

    // Verify redirection to the login page
    cy.url().should('eq', loginPageUrl);
  });
});
