describe('MainPage Tests', () => {
  beforeEach(() => {
    // Set up the auth token to simulate a logged-in user
    localStorage.setItem('authToken', 'test-token');
    cy.visit('/mainpage');
  });

  it('redirects unauthorized users to the login page', () => {
    // Simulate an unauthorized user by removing the auth token
    localStorage.removeItem('authToken');
    cy.visit('/mainpage');
  });

  it('renders Navbar, Sidebar, and Footer', () => {
    cy.get('.navbar-dark-gray').should('be.visible'); // Navbar
    cy.get('.sidebar').should('be.visible');         // Sidebar
    cy.get('.footer', { timeout: 10000 }).should('be.visible'); // Footer
  });

  it('displays the default view with no posts or bookmarks', () => {
    cy.get('.content-box').should('not.contain', 'Posts');
    cy.get('.content-box').should('not.contain', 'Bookmarks');
  });

  it('displays New Post section when clicked', () => {
    cy.get('.sidebar').contains('NEW POST').click();
    cy.get('.new-post').should('contain', 'Create your Blog');
  });

  it('does not allow accessing posts without a valid token', () => {
    cy.intercept('GET', 'http://localhost:9595/mainpage', {
      statusCode: 401, 
    }).as('fetchPosts');
    localStorage.removeItem('authToken');
    cy.reload();
  });

});
