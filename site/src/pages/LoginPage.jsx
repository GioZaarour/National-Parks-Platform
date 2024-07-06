import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import logo from '../img/310logo.png';

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [failedAttempts, setFailedAttempts] = useState(0);
  const [lastFailedAttempt, setLastFailedAttempt] = useState(null);
  const navigate = useNavigate();
  const [lockedOut, setLockedOut] = useState(false)

  useEffect(() => {
    const storedFailedAttempts = localStorage.getItem("failedAttempts");
    const storedLastFailedAttempt = localStorage.getItem("lastFailedAttempt");

    if (storedFailedAttempts) {
      setFailedAttempts(parseInt(storedFailedAttempts));
    }

    if (storedLastFailedAttempt) {
      setLastFailedAttempt(parseInt(storedLastFailedAttempt));
    }
  }, []);

  useEffect(() => {
    localStorage.setItem("failedAttempts", failedAttempts);
    localStorage.setItem("lastFailedAttempt", lastFailedAttempt);
  }, [failedAttempts, lastFailedAttempt]);

  useEffect(() => {
    setLockedOut(false);
    let timeout;

    if (failedAttempts >= 3 && Date.now() - lastFailedAttempt < 30000) {
      setLockedOut(true);
      setErrorMessage("Too many failed attempts. Please try again later.");

      // Set a timeout to unlock after 30 seconds
      timeout = setTimeout(() => {
        setLockedOut(false);
      }, 30000);
    } else if (failedAttempts > 0 && failedAttempts < 3) {
      setErrorMessage(`Invalid credentials. ${3 - failedAttempts} attempt(s) remaining.`);
    } else if (failedAttempts > 0 && Date.now() - lastFailedAttempt >= 60000) {
      setFailedAttempts(0);
      setLastFailedAttempt(null);
      setErrorMessage("");
    } else {
      setErrorMessage("");
    }

    // Clean up the timeout if the component unmounts or the effect re-runs
    return () => {
      clearTimeout(timeout);
    };
  }, [failedAttempts, lastFailedAttempt]);

  const handleLogin = async (event) => {
    event.preventDefault();

    // Check if the user is currently locked out
    if (failedAttempts >= 3 && Date.now() - lastFailedAttempt < 30000) {
      return;
    }

    const loginData = {
      username: username,
      password: password,
    };

    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });

      if (response.ok) {
        const result = await response.json();
        console.log('Login successful:', result);
        setFailedAttempts(0);
        setLastFailedAttempt(null);
        navigate('/search');

      } else {
        console.error('Login failed:', response.statusText);
        const errorText = await response.text();
        setFailedAttempts(failedAttempts + 1);
        setLastFailedAttempt(Date.now());
      }
    } catch (error) {
      console.error('Network error:', error);
      setErrorMessage("Network error: " + error);
      setFailedAttempts(failedAttempts + 1);
      setLastFailedAttempt(Date.now());
    }
  };

  return (
    <div className={"main-container"}>
      <div className={"row d-flex justify-content-center align-items-center h-100 m-0"}>
        <div className={"col-12 mx-auto p-0"}>
          <div className={"container login-container"}>
            <div className={"row login-bar"}></div>
            <div className={"p-5"}>
              <div className={"row mb-3"}>
                <div className={"col-12 text-center"}>
                  <span>
                    <img src={logo} alt={"logo"} className={"img-fluid login-logo"} />
                  </span>
                </div>
              </div>
              <form id={"login"} onSubmit={handleLogin}>
                <div className={"form-group row"}>
                  <div className={"col-12 mt-2"}>
                    <div className={"input-block pt-3"}>
                      <input
                        type={"text"}
                        name={"username"}
                        id={"username"}
                        required
                        className={"form-control"}
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        autoComplete={"username"}
                      />
                      <span className={"placeholder"}>Username</span>
                    </div>
                  </div>
                </div>
                <div className={"form-group row"}>
                  <div className={"col-12 mb-2"}>
                    <div className={"input-block pt-3"}>
                      <input
                        type={"password"}
                        name={"password"}
                        id={"password"}
                        required
                        className={"form-control"}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        autoComplete={"current-password"}
                      />
                      <span className={"placeholder"}>Password</span>
                    </div>
                  </div>
                </div>
                <div className={"form-group row"}>
                  <div className={"col-12 mb-2"}>
                    {errorMessage!='' && (
                    <span id={'error'} className={"text-danger"}>
                                          {errorMessage}
                                        </span>)}
                  </div>
                </div>
                <div className={"form-group row"}>
                  <div className={"col-12"}>
                    <button
                      type={"submit"}
                      className={"btn colored-button w-100 h-100"}
                      id={"loginButton"}
                      disabled = {lockedOut}
                    >
                      Log In
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
                    <small>
                      New User? <a href={"/signup"} className={"signup-link"}>
                        Sign Up!
                      </a>
                    </small>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
        <span className={"team-footer py-3 px-3 py-sm-5 px-sm-5"}>Team 27</span>
      </div>
    </div>
  );
}

export default LoginPage;