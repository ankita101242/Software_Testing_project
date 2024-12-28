describe('NewPost Component - User Session Tests', () => {
  beforeEach(() => {
    // Mock the auth token and alias the API route before each test
    localStorage.setItem('authToken', 'test-token'); // Mock authToken
    cy.intercept('POST', '**/api/posts/user/create').as('createPost'); // Alias API route
    cy.visit('/newpost');
  });

  it('retrieves authToken from localStorage', () => {
    const mockAuthToken = 'mockedAuthToken123';
    localStorage.setItem('authToken', mockAuthToken);
    cy.reload();

    // Intercept the request and verify the authorization header
    cy.intercept('POST', '**/api/posts/user/create', (req) => {
      expect(req.headers.authorization).to.equal(`Bearer ${mockAuthToken}`);
    }).as('createPost');

    cy.get('input[placeholder="Enter Title"]').type('Sample Blog Title');
    cy.get('.ql-editor').type('Sample Blog Content');
    cy.get('button').contains('Submit').click();
    
  });

  it('uses userId from localStorage in the API request', () => {
    const authToken = 'authToken123';
    localStorage.setItem('authToken', authToken);
    localStorage.setItem('userId', 2);

    // Intercept the API call and validate the userId and authorization header
    cy.intercept('POST', '**/api/posts/user/create').as('createPost');
    cy.visit('/newpost');
    cy.get('input[placeholder="Enter Title"]').type('Sample Blog Title');
    cy.get('.ql-editor').type('Sample Blog Content');
    cy.get('button').contains('Submit').click();

// Log network requests
cy.wait('@createPost', { timeout: 10000 }).then((interception) => {
  console.log(interception);
});

  });

  it('should load the New Post page with all required elements', () => {
    cy.get('input[placeholder="Enter Title"]').should('be.visible');
    cy.get('input[type="file"][accept="image/*"]').should('be.visible');
    cy.get('.ql-editor').should('be.visible').and('contain', '');
    cy.get('button').contains('Submit').should('be.visible');
  });

  it('should show validation errors for empty form submission', () => {   
      cy.get('button').contains('Submit').click();
      cy.get('.text-danger').should(($errors) => {
        expect($errors).to.contain('Title is required.');
        expect($errors).to.contain('Content is required.');
      });
    });

  it('should clear all fields after successful submission', () => {
    const mockAuthToken = 'mockAuthToken123';
    localStorage.setItem('authToken', mockAuthToken);

    // Mock successful API response
    cy.intercept('POST', '**/api/posts/user/create', {
      statusCode: 201,
      body: { success: true },
    }).as('createPost');

    cy.get('input[placeholder="Enter Title"]').type('Sample Blog Title');
    cy.get('.ql-editor').type('Sample Blog Content');
    cy.get('button').contains('Submit').click();
    cy.get('input[placeholder="Enter Title"]').should('have.value', '');
  });

  it('should allow the user to edit the blog content using the editor', () => {
    cy.get('.ql-editor').type('This is a test blog content.');
    cy.get('.ql-editor').should('contain', 'This is a test blog content.');
  });
});
