export const paramsSerializer = (params) => {
    const searchParams = new URLSearchParams();
    Object.keys(params).forEach(key => {
        if (Array.isArray(params[key])) {
            params[key].forEach(value => {
                searchParams.append(key, value);
            });
        } else {
            searchParams.append(key, params[key]);
        }
    });
    return searchParams.toString();
};


