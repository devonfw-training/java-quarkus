import { keycloak } from "../auth/keycloak";

/**
 * Logs out the current user.
 *
 * The backend builds the URL of the Keycloak end-session endpoint. Sending the browser there ends the session of the user in Keycloak,
 * so the refresh token becomes useless and the (short-lived) access token cannot be renewed anymore.
 * If the backend cannot be reached or refuses the request (e.g. because the access token has already expired),
 * the logout is done directly via Keycloak.
 */
export const logout = async () => {
    try {
        await keycloak.updateToken(30);

        const response = await fetch("/api/auth/logout", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${keycloak.token}`,
                "Content-Type": "application/json",
            },
            // sent in the body (not in the URL) so that it does not end up in logs or the browser history
            body: JSON.stringify({ idToken: keycloak.idToken }),
        });

        if (response.ok) {
            const { logoutUrl } = await response.json();
            window.location.href = logoutUrl;
            return;
        }
        console.error("Logout via backend failed:", response.status);
    } catch (error) {
        console.error("Logout via backend failed:", error);
    }

    keycloak.logout({ redirectUri: window.location.origin });
};
