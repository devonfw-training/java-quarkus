import Keycloak from "keycloak-js";

export const keycloak = new Keycloak({
    url: "http://localhost:8180",
    realm: "quarkus",
    clientId: "frontend",
});

let initialized = false;

export const initKeycloak = async () => {
    if (initialized) {
        return keycloak.authenticated ?? false;
    }

    initialized = true;

    return keycloak.init({
        onLoad: "login-required",
        pkceMethod: "S256",
        scope: "openid profile email",
        checkLoginIframe: false,
        enableLogging: true,
    });
};
