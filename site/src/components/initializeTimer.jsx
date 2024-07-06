let timeoutId;

export const startInactivityTimer = (callback, timeout) => {
    resetInactivityTimer();
    timeoutId = setTimeout(callback, timeout);
};

export const resetInactivityTimer = () => {
    clearTimeout(timeoutId);
};