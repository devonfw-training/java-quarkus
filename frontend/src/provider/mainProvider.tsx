import { createContext, useState } from "react";
import { initKeycloak, keycloak } from "../auth/keycloak";
import { useEffect } from "react";

export const MainContext = createContext<MainContextI | null>(null);

export const MainProvider = ({children}: PropsI) => {
    const [errorAlert, setErrorAlert] = useState("");
    const [successAlert, setSuccessAlert] = useState("");

    const [showSettings, setShowSettings] = useState<boolean>(false);
    const [showAbout, setShowAbout] = useState<boolean>(false);
    const [showCalendar, setShowCalendar] = useState<boolean>(false);

    const changeShowCalendar = () => {
        setShowCalendar(!showCalendar);
    };

    const [keycloakReady, setKeycloakReady] = useState<boolean>(false);
    const [authenticated, setAuthenticated] = useState<boolean>(false);

    useEffect(() => {
        initKeycloak()
            .then((auth) => {
                setKeycloakReady(true);
            })
            .catch((error) => {
                console.error("Keycloak initialization failed - raw error:", error);
                console.error("Current URL:", window.location.href);
                console.error("Origin:", window.location.origin);
                console.error("Keycloak token endpoint should be reachable via:");
                console.error("http://localhost:8180/realms/quarkus/.well-known/openid-configuration");

                setErrorAlert("Keycloak konnte nicht initialisiert werden.");
                setKeycloakReady(true);
            });

    }, []);

    if (!keycloakReady) {
        return <div>Loading...</div>;
    }

    return (
        <MainContext.Provider
            value={{
                errorAlert,
                successAlert,
                showSettings,
                showAbout,
                showCalendar,
                setErrorAlert,
                setSuccessAlert,
                setShowSettings,
                setShowAbout,
                changeShowCalendar,
            }}
        >
            {children}
        </MainContext.Provider>
    );
};
