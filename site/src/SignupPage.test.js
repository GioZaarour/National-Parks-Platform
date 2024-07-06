import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router} from 'react-router-dom';
import SignupPage from './pages/SignupPage'; // Adjust the import path as necessary
import fetchMock from 'jest-fetch-mock';
import '@testing-library/jest-dom';
import { createMemoryHistory } from 'history';

fetchMock.enableMocks();

describe('SignupPage Component', () => {
    let container = null;
    let navigateMock;

    beforeEach(() => {
        const history = createMemoryHistory();
        navigateMock = jest.fn();
        const rendered = render(
            <Router history={history}>
                <SignupPage navigate={navigateMock} />
            </Router>
        );
        container = rendered.container;
        fetchMock.resetMocks();
    });

    test('renders signup page correctly', () => {
        expect(container.querySelector('input#username')).toBeInTheDocument();
        expect(container.querySelector('input#password')).toBeInTheDocument();
        expect(container.querySelector('input#confirm-password')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Sign Up/i })).toBeInTheDocument();
        expect(screen.getByAltText('logo')).toBeInTheDocument();
    });

//    test('allows user to enter username, password, and confirm password', async () => {
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password123');
//        await userEvent.type(confirmPasswordField, 'password123');
//
//        expect(usernameField.value).toBe('testUser');
//        expect(passwordField.value).toBe('password123');
//        expect(confirmPasswordField.value).toBe('password123');
//    });
//
//    test('submits form and handles response correctly', async () => {
//        fetchMock.mockResponseOnce(JSON.stringify({ success: true }));
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signUpButton = screen.getByRole('button', { name: /Sign Up/i });
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'password123');
//        await userEvent.type(confirmPasswordField, 'password123');
//        userEvent.click(signUpButton);
//
//            // This waits for the fetch to be called after clicking the sign-up button
//        await waitFor(() => expect(fetchMock).toHaveBeenCalled());
//
//        // Additional assertion to check if fetch was called with the correct URL and payload
//        expect(fetchMock).toHaveBeenCalledWith(expect.anything(), expect.objectContaining({
//            method: 'POST',
//            body: JSON.stringify({
//                username: 'testUser',
//                password: 'password123',
//                confirmPassword: 'password123'
//            })
//        }));
//
//        // This assumes you have a mechanism to wait for asynchronous actions to complete if necessary.
//        //await waitFor(() => expect(fetchMock).toHaveBeenCalled());
//
//        // Additional specific checks for fetchMock, like checking the request body, can be added here
//    });
//
//    test('displays error message when password and confirm password do not match', async () => {
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signUpButton = screen.getByRole('button', { name: /Sign Up/i });
//
//        await userEvent.type(passwordField, 'password123');
//        await userEvent.type(confirmPasswordField, 'differentPassword');
//        userEvent.click(signUpButton);
//
//        // Await for the error message to appear in the document
//        const errorMessage = await screen.findByText(/Password and confirm password do not match/i);
//        expect(errorMessage).toBeInTheDocument();
//    });
//
//    test('displays error message when password does not meet requirements', async () => {
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signUpButton = screen.getByRole('button', { name: /Sign Up/i });
//
//        await userEvent.type(passwordField, 'weakpassword');
//        await userEvent.type(confirmPasswordField, 'weakpassword');
//        userEvent.click(signUpButton);
//
//        // Await for the error message to appear in the document
//        const errorMessage = await screen.findByText(/Password must contain at least one uppercase letter, one lowercase letter, and one symbol/i);
//        expect(errorMessage).toBeInTheDocument();
//    });
//
//    test('handles network error during signup', async () => {
//        const networkError = new Error('Network error');
//        fetchMock.mockRejectOnce(networkError);
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signupForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'Password123!');
//        await userEvent.type(confirmPasswordField, 'Password123!');
//        fireEvent.submit(signupForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/signup', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'Password123!' }),
//        });
//
//        await screen.findByText(/Network error:/);
//
//        expect(screen.getByText(/Network error:/)).toBeInTheDocument();
//        expect(screen.getByText(/Network error:/).textContent).toContain(networkError.message);
//    });
//
//    test('handles server error during signup', async () => {
//        const errorResponse = 'Internal Server Error';
//        fetchMock.mockResponseOnce(errorResponse, { status: 500 });
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signupForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'Password123!');
//        await userEvent.type(confirmPasswordField, 'Password123!');
//        fireEvent.submit(signupForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/signup', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'Password123!' }),
//        });
//
//        await screen.findByText(/Sign up failed:/);
//
//        expect(screen.getByText(/Sign up failed:/).textContent).toContain(errorResponse);
//    });
//
//    test('handles successful signup', async () => {
//        const signupResponse = { success: true };
//        fetchMock.mockResponseOnce(JSON.stringify(signupResponse), { status: 200 });
//
//        const usernameField = container.querySelector('input#username');
//        const passwordField = container.querySelector('input#password');
//        const confirmPasswordField = container.querySelector('input#confirm-password');
//        const signupForm = container.querySelector('form');
//
//        await userEvent.type(usernameField, 'testUser');
//        await userEvent.type(passwordField, 'Password123!');
//        await userEvent.type(confirmPasswordField, 'Password123!');
//        fireEvent.submit(signupForm);
//
//        expect(fetchMock).toHaveBeenCalledTimes(1);
//        expect(fetchMock).toHaveBeenCalledWith('/api/signup', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ username: 'testUser', password: 'Password123!' }),
//        });
//
//        await waitFor(() => expect(window.location.pathname).toBe('/login'));
//    });
//
//    test('updates goBack state when "Cancel Signup" button is clicked', async () => {
//        const cancelSignupButton = screen.getByRole('button', { name: /Cancel Signup/i });
//
//        userEvent.click(cancelSignupButton);
//
//        // Wait for the "Confirm Cancel" button to appear in the document
//        await screen.findByRole('button', { name: /Confirm Cancel/i });
//
//        expect(screen.getByRole('button', { name: /Confirm Cancel/i })).toBeInTheDocument();
//        expect(screen.getByRole('button', { name: /Go Back/i })).toBeInTheDocument();
//    });
//
//    test('navigates to login page when "Confirm Cancel" button is clicked', async () => {
//        const cancelSignupButton = screen.getByRole('button', { name: /Cancel Signup/i });
//
//        userEvent.click(cancelSignupButton);
//
//        const confirmCancelButton = await screen.findByRole('button', { name: /Confirm Cancel/i });
//        userEvent.click(confirmCancelButton);
//
//        await waitFor(() => expect(window.location.pathname).toBe('/login'));
//    });
//
//    test('updates goBack state when "Go Back" button is clicked', async () => {
//        const cancelSignupButton = screen.getByRole('button', { name: /Cancel Signup/i });
//
//        userEvent.click(cancelSignupButton);
//
//        const goBackButton = await screen.findByRole('button', { name: /Go Back/i });
//        userEvent.click(goBackButton);
//
//        // Use waitFor to ensure the UI has updated after the asynchronous operation
//        await waitFor(() => {
//            expect(screen.queryByRole('button', { name: /Confirm Cancel/i })).not.toBeInTheDocument();
//        });
//
//        // No need to use waitFor again if the state update is expected to happen at the same time
//        expect(screen.queryByRole('button', { name: /Go Back/i })).not.toBeInTheDocument();
//    });
//
//    test('navigates to login page when "Confirm Cancel" button is clicked', async () => {
//        // Action: Find and click the "Confirm Cancel" button once it's available
//        // Assuming a preceding action makes the "Confirm Cancel" button appear
//        const cancelSignupButton = screen.getByRole('button', { name: /Cancel Signup/i });
//        userEvent.click(cancelSignupButton);
//
//        const confirmCancelButton = await screen.findByRole('button', { name: /Confirm Cancel/i });
//        await userEvent.click(confirmCancelButton); // Use await if the click leads to asynchronous updates
//
//        // Assertion: Check if the navigate function was called with the correct argument
//        // This assumes navigateMock is a mock function that captures calls for verification
//    });
});