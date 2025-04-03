import Keycloak from 'keycloak-js';

// Define the Keycloak configuration
const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL as string,     
  realm: import.meta.env.VITE_KEYCLOAK_REALM as string,     
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID as string,
});
export const initKeycloak = (onAuthenticatedCallback: () => void) => {
  keycloak.init({ onLoad: 'login-required' }).then((authenticated) => {
    if (authenticated) {
      console.log('Keycloak authenticated');

      // Save the token in localStorage
      localStorage.setItem('keycloakToken', keycloak.token!);

      onAuthenticatedCallback();
    } else {
      console.warn('Keycloak authentication failed');
      window.location.reload();
    }
  }).catch((error) => {
    console.error('Keycloak initialization failed:', error);
  });
};

export const getKeycloak = () => keycloak;

export const logoutKeycloak = () => {
    localStorage.removeItem('keycloakToken');
    localStorage.removeItem('keycloakRoles');
  
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  };