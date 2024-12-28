describe('Homepage Tests', () => {
    it('Navigation to Home Page', () => {
      cy.visit('/homepage');
      cy.url().should('include', '/homepage');
      cy.title().should('eq', 'Prose Petal');
    });
  beforeEach(() => {
    // Set auth token in localStorage before visiting the page
    localStorage.setItem('authToken', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWphbEBwcm9zZXBld…nF9ccl9IbtGZF2xQWKwj-14g0GJfeE0Vas-vBRB2u6m1zkbJg');
    cy.visit('/homepage');
  });

  // Test: Homepage loads and displays content
  it('load the homepage and display the articles section', () => {
    cy.contains('ARTICLES').should('be.visible');
  });

  // Test: Carousel functionality
  it('should display the carousel with images', () => {
    cy.get('.carousel').should('be.visible');
    cy.get('.carousel img').should('have.length.greaterThan', 0);
    cy.get('.carousel-control-next').click();
    cy.get('.carousel-control-prev').click();
  });

  // Test: Fetching articles
  it('fetching and display articles', () => {
    cy.intercept('GET', 'http://localhost:9595/api/posts/all', {
      statusCode: 200,
      body: [
        { postId: 1, title: 'First Post', content: 'This is the first post.', imageName: '/path/to/image1.jpg' },
        { postId: 2, title: 'Second Post', content: 'This is the second post.', imageName: '/path/to/image2.jpg' },
      ],
    });
    cy.reload();
  });

  // Test: Unauthorized access (No Auth Token)
  it('should redirect to the login page if no auth token is found', () => {
    cy.intercept('GET', 'http://localhost:9595/api/posts/all', {
      statusCode: 401, 
    }).as('fetchPosts');

    localStorage.removeItem('authToken');
    cy.visit('/homepage');
    cy.url().should('include', '/login');
  });

  // Test: Logout functionality
  it('log out and redirect to the login page', () => {
    cy.contains('ARTICLES').should('be.visible');
    cy.get('.nav-link').contains('LOGOUT').click(); 
    cy.url().should('include', '/login');
    cy.visit('/homepage');
    cy.url().should('include', '/login');
  });

  // Test: Invalidate session across multiple tabs on logout
  it('invalidate session across multiple tabs on logout', () => {
    cy.visit('/homepage');
    cy.url().should('include', '/homepage');
    cy.window().then((window) => {
      window.localStorage.removeItem('authToken');
    });
    cy.reload();
    cy.url().should('include', '/login');
  });
});