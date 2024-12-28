import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import axios from 'axios';
import LoginPage from './LoginPage';

jest.mock('axios');

describe('LoginPage', () => {
  test('renders login form elements', () => {
    render(
      <Router>
        <LoginPage />
      </Router>
    );

    const emailInput = screen.getByLabelText(/email address/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /submit/i });

    expect(emailInput).toBeInTheDocument();
    expect(passwordInput).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();
  });

  test('displays error message when invalid credentials are provided', async () => {
    axios.post.mockRejectedValueOnce({
      response: { status: 401 }
    });

    render(
      <Router>
        <LoginPage />
      </Router>
    );

    fireEvent.change(screen.getByLabelText(/email address/i), { target: { value: 'wrong@domain.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'incorrectpassword' } });
    fireEvent.click(screen.getByRole('button', { name: /submit/i }));
  });

  test('navigates to homepage on successful login', async () => {
    axios.post.mockResolvedValueOnce({
      data: { token: 'fakeToken', user_id: '123' }
    });

    render(
      <Router>
        <LoginPage />
      </Router>
    );

    fireEvent.change(screen.getByLabelText(/email address/i), { target: { value: 'test@domain.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'correctpassword' } });
    fireEvent.click(screen.getByRole('button', { name: /submit/i }));

  });

  test('displays a generic error message on unexpected error', async () => {
    axios.post.mockRejectedValueOnce(new Error('Unexpected error'));

    render(
      <Router>
        <LoginPage />
      </Router>
    );

    fireEvent.change(screen.getByLabelText(/email address/i), { target: { value: 'error@domain.com' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'errorpassword' } });
    fireEvent.click(screen.getByRole('button', { name: /submit/i }));

    await waitFor(() => {
      const errorMessage = screen.getByText(/an unexpected error occurred. please try again/i);
      expect(errorMessage).toBeInTheDocument();
    });
  });
});
