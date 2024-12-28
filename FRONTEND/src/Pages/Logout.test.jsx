import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom'; // For routing
import NavbarComponent from '../Components/NavbarComponent';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

describe('NavbarComponent - Logout Behavior', () => {
  let mockNavigate;

  beforeEach(() => {
    mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);

    Storage.prototype.removeItem = jest.fn();
    Cookies.remove = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('redirects to login page on logout', () => {
    render(
      <Router>
        <NavbarComponent />
      </Router>
    );

    const logoutButton = screen.getByText(/logout/i);
    fireEvent.click(logoutButton);

    expect(mockNavigate).toHaveBeenCalledWith('/login');
    
    expect(localStorage.removeItem).toHaveBeenCalledWith('authToken');
    expect(localStorage.removeItem).toHaveBeenCalledWith('email');
    expect(sessionStorage.removeItem).toHaveBeenCalledWith('authToken');
    expect(sessionStorage.removeItem).toHaveBeenCalledWith('email');
    expect(Cookies.remove).toHaveBeenCalledWith('authToken');
    expect(Cookies.remove).toHaveBeenCalledWith('email');
  });
});
