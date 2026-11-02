import { keycloak } from "../auth/keycloak";

export const logout = () => {
    const rawIdToken = keycloak.idToken;

    console.log("Logout - ID Token:", rawIdToken);

    if (!rawIdToken) {
        console.error("Kein ID Token vorhanden. Logout nicht möglich.");
        return;
    }

    window.location.href = `/api/auth/logout?idToken=${encodeURIComponent(rawIdToken)}`;
};
