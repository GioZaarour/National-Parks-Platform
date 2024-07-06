// Import statements
import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, Navigate, Outlet } from "react-router-dom";
import axios from 'axios';
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import SearchParksPage from "./pages/SearchParksPage";
import SuggestParkPage from "./pages/SuggestParkPage";
import FavoritesPage from "./pages/FavoritesPage";
import CompareFavoritesPage from "./pages/CompareFavoritesPage";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./styles/index.css";
import reportWebVitals from "./reportWebVitals";

// ProtectedRoute component
const ProtectedRoute = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(null);

    useEffect(() => {
        const checkAuth = async () => {
            console.log("Checking authentication status...");
            try {
                const response = await axios.get('http://localhost:8080/api/checkSession');
                console.log("Server response:", response.data);
                setIsAuthenticated(response.data !== null && response.data !== "");
            } catch (error) {
                console.error("Session check failed:", error);
                setIsAuthenticated(false);
            }
        };

        checkAuth();
    }, []);

    if (isAuthenticated === null) {
        return <div>Loading...</div>;
    }

    return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route element={<ProtectedRoute />}>
                    <Route path="/search" element={<SearchParksPage />} />
                    <Route path="/suggest" element={<SuggestParkPage />} />
                    <Route path="/favorites" element={<FavoritesPage />} />
                    <Route path="/compare" element={<CompareFavoritesPage />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);

reportWebVitals();