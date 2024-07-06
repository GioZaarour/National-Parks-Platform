import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { startInactivityTimer, resetInactivityTimer } from "./initializeTimer";

function InactivityTimer({ timeout }) {
    const navigate = useNavigate();

    useEffect(() => {
        const handleActivity = () => {
            resetInactivityTimer();
            startInactivityTimer(handleLogout, timeout);
        };

        window.addEventListener("mousemove", handleActivity);
        window.addEventListener("keydown", handleActivity);

        startInactivityTimer(handleLogout, timeout);

        return () => {
            window.removeEventListener("mousemove", handleActivity);
            window.removeEventListener("keydown", handleActivity);
            resetInactivityTimer();
        };
    }, [timeout]);

    const handleLogout = async () => {
        try {
            const response = await fetch('/api/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                console.log('Logout successful');
                navigate("/login");
            } else {
                console.error('Logout failed:', response.statusText);
            }
        } catch (error) {
            console.error('Network error:', error);
        }
    };

    return null;
}

export default InactivityTimer;