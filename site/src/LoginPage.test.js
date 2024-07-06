
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import LoginPage from './pages/LoginPage.jsx';
import fetchMock from 'jest-fetch-mock';
import '@testing-library/jest-dom'

fetchMock.enableMocks();

describe('LoginPage Component', () => {
    let container = null;



    beforeEach(() => {
        // Destructure container from the render method to use it in querying DOM elements directly
        const rendered = render(<LoginPage />, { wrapper: Router });
        container = rendered.container;
        fetchMock.resetMocks();
    });

    test('renders login page correctly', () => {
        expect(container.querySelector('input#username')).toBeInTheDocument();
        expect(container.querySelector('input#password')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /log in/i })).toBeInTheDocument();
        expect(screen.getByAltText('logo')).toBeInTheDocument();
    });

//    test('allows user to enter username and password', async () => {
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password');
//
//        expect(usernameField.value).toBe('testUser');
//        expect(passwordField.value).toBe('password');
//    });
//
//    test('submits form and handles response correctly', async () => {
//        fetchMock.mockResponseOnce(JSON.stringify({ token: 'fake-token' }));
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const loginButton = screen.getByRole('button', { name: /log in/i});
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password');
//        userEvent.click(loginButton);
//
//        // Wait for the fetch to be called after clicking the login button
//        await waitFor(() => expect(fetchMock).toHaveBeenCalled());
//
//        // Check if fetch was called with the correct URL and request body
//        expect(fetchMock).toHaveBeenCalledWith(expect.anything(), expect.objectContaining({
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({
//                username: 'testUser',
//                password: 'password'
//            })
//        }));
//
//        // window.Navigator("/homepage");
//
//        // Here you would typically check the mock's calls to ensure the form was submitted as expected.
//        // For instance, you might want to check fetchMock was called with the correct arguments.
//        // This assumes you have a mechanism to wait for asynchronous actions to complete if necessary.
//        // expect(fetchMock).toHaveBeenCalled();
//        // Further expectations on fetchMock calls can be added here.
//    });
//
//    test('handles network error during login', async () => {
//        const networkError = new Error('Network error');
//        fetchMock.mockRejectOnce(networkError);
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const loginForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password');
//        fireEvent.submit(loginForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/login', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'password' }),
//        });
//
//        // Wait for the error message to appear
//        await screen.findByText(/Network error:/);
//
//        expect(screen.getByText(/Network error:/)).toBeInTheDocument();
//        expect(screen.getByText(/Network error:/).textContent).toContain(networkError.message);
//    });
//
//    test('handles server error during login', async () => {
//        const errorResponse = 'Internal Server Error';
//        fetchMock.mockResponseOnce(errorResponse, { status: 500 });
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const loginForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password');
//        fireEvent.submit(loginForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/login', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'password' }),
//        });
//
//        // Wait for the error message to appear
//        await screen.findByText(/Log in failed:/);
//
//        expect(screen.getByText(/Log in failed:/).textContent).toContain(errorResponse);
//    });
//
//    test('handles successful login', async () => {
//        const loginResponse = { token: 'fake-token' };
//        fetchMock.mockResponseOnce(JSON.stringify(loginResponse), { status: 200 });
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const loginForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password');
//        fireEvent.submit(loginForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/login', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'password' }),
//        });
//
//        // Wait for the navigation to complete
//        await waitFor(() => expect(window.location.pathname).toBe('/search'));
//    });


});