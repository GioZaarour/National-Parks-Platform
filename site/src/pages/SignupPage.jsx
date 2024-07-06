import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import logo from '../img/310logo.png'

function SignupPage() {
    const [ fetchResponse, handleFetchResponse ] = useState();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState(""); // Add a state for confirm password
    const [goBack, setGoBack] = useState("False");
    const [errorMessage, setErrorMessage] = useState(""); // Add an error message state



    const handleSignUp = async (event) => {
        event.preventDefault(); // Prevent the form from refreshing the page

        // Check if password and confirm password are equal
        if (password !== confirmPassword) {
            setErrorMessage("Password and confirm password do not match.");
            return; // Stop the function if the passwords do not match
        }

        // check that password has an uppercase, a lowercase, and a symbol in it. else give error and return
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasSymbol = /[^A-Za-z0-9]/.test(password);
        if (!hasUppercase || !hasLowercase || !hasSymbol) {
            setErrorMessage("Password must contain at least one uppercase letter, one lowercase letter, and one symbol.");
            return;
        }

        const userData = {
            username: username,
            password: password,
        };

        try {
            const response = await fetch('/api/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Sign up successful:', result);
                // Handle successful sign up (e.g., navigate to login page)
                navigate('/login'); // Assuming you have set up react-router-dom
            } else {
                // Handle server errors or unsuccessful sign up
                console.error('Sign up failed:', response.statusText);
                const errorText = await response.text();
                setErrorMessage(`Sign up failed: ${errorText}`);
            }
        } catch (error) {
            // Handle network errors
            //console.log(response);
            console.log(JSON.stringify(userData));
            console.error('Network error:', error);
            setErrorMessage("Network error: " + error);
        }
    };

    return (
        <div className={"main-container"}>
            <div className={"row d-flex justify-content-center align-items-center h-100 m-0"}>
                <div className={"col-12 mx-auto p-0"}>
                    <div className={"container login-container"}>
                        <div className={"row login-bar"}>
                        </div>

                        <div className={"p-5"}>
                            <div className={"row mb-3"}>
                                <div className={"col-12 text-center"}>
                                    <span>
                                        <img src={logo} alt={"logo"}
                                             className={"img-fluid login-logo"}/>
                                    </span>
                                </div>
                            </div>

                            <form id={"signup"} onSubmit={handleSignUp}>
                                <div className={"form-group row"}>
                                    <div className={"col-12 mt-2"}>
                                        <div className={"input-block pt-3"}>
                                            <input type={"text"}
                                                   name={"username"}
                                                   id={"username"}
                                                   required
                                                   className={"form-control"}
                                                   value={username}
                                                    onChange={(e) => setUsername(e.target.value)}
                                                   autoComplete={"username"}/>
                                            <span className={"placeholder"}>
                                                Username
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <div className={"col-12"}>
                                        <div className={"input-block pt-3"}>
                                            <input type={"password"}
                                                   name={"password"}
                                                   id={"password"}
                                                   required
                                                   className={"form-control"}
                                                   value={password}
                                                    onChange={(e) => setPassword(e.target.value)}
                                                   autoComplete={""}/>
                                            <span className={"placeholder"}>
                                                Password
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <div className={"col-12 mb-2"}>
                                        <div className={"input-block pt-3"}>
                                            <input type={"password"}
                                                   name={"confirm-password"}
                                                   id={"confirm-password"}
                                                   required
                                                   className={"form-control"}
                                                   value={confirmPassword}
                                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                                   autoComplete={""}/>
                                            <span className={"placeholder"}>
                                                Confirm Password
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <div className={"col-12 mb-2"}>
                                        <span id={'error'}
                                              className={"text-danger"}>
                                                {errorMessage}
                                        </span>
                                    </div>
                                </div>

                                <div className={"form-group row"}>
                                    <div className={"col-12"}>
                                        <button type={"submit"}
                                                className={"btn colored-button w-100 h-100"}
                                                id={"signupButton"}>
                                            Sign Up
                                        </button>
                                    </div>
                                </div>

                                <div className={"row mb-3"}>
                                    <div className={"col-12 text-center with-line"}>
                                        <span>or</span>
                                    </div>
                                </div>

                                <div className={"row"}>
                                    <div className={"col-12 text-center"}>
                                        <small><button id={"cancel-signup"}
                                            onClick={() => setGoBack("True")}
                                            className={"btn btn-danger w-100 h-100"}>Cancel Signup</button></small>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>


                { goBack === "True" && (
                    <div className={"confirm-go-back"}>
                        <div className={"login-container d-flex flex-column p-4 confirm-go-back-position"}>
                            <button className={"btn btn-danger"} id={"confirm-cancel-signup"}
                            onClick={() => navigate('/login')}>Confirm Cancel</button>
                            <button className={"btn colored-button mt-3"} id = {"cancel-cancel-signup"}
                            onClick={() => setGoBack("False")}>Go Back</button>
                        </div>
                    </div>
                )}
            </div>
            <span className={"team-footer py-3 px-3 py-sm-5 px-sm-5"}>
                                Team 27
                            </span>
        </div>
    )
}


export default SignupPage;
