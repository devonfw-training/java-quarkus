const getAuthToken = (): string | null => {
    const token = localStorage.getItem('keycloakToken');
    return token;
};

export const addHeaders = (headers: HeadersInit = {}): HeadersInit => {
    const token = getAuthToken();

    const headersObj = new Headers(headers);
    headersObj.set('Accept', 'application/json');
    headersObj.set('Content-Type', 'application/json');

    if (token) {
        headersObj.set('Authorization', `Bearer ${token}`);
        return headersObj;
    }

    return headers;
};