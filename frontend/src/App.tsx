import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { useContext, useEffect } from "react";
import { Route } from "wouter";
import CalendarView from "./components/calendar";
import Header from "./components/misc/header";
import Sidebar from "./components/misc/sidebar";
import { MainContext } from "./provider/mainProvider";
import Todos from "./components/todos/Todos";

const COOKIE_EXPIRATION_TIME = 3600 * 1000; // 1 hour in milliseconds

function App() {
  const {
    errorAlert,
    setErrorAlert,
    successAlert,
    setSuccessAlert,
    showCalendar,
  } = useContext(MainContext)!;

  // Function to generate a new session ID
  const generateSessionId = () => {
    return crypto.randomUUID();
  };

  // Function to set a session cookie
  const setSessionCookie = () => {
    const newSessionId = generateSessionId();
    document.cookie = `SESSION_ID=${newSessionId}; path=/; max-age=3600; Secure; SameSite=None`;
    console.log("New SESSION_ID set:", newSessionId);
  };

  // Function to get an existing cookie
  const getCookie = (name: string) => {
    const cookies = document.cookie.split("; ");
    for (let cookie of cookies) {
      const [key, value] = cookie.split("=");
      if (key === name) {
        return value;
      }
    }
    return null;
  };

  // Runs once on app load
  useEffect(() => {
    // If SESSION_ID doesn't exist, create one
    if (!getCookie("SESSION_ID")) {
      setSessionCookie();
    }

    // Set an interval to renew the session cookie when it expires
    const interval = setInterval(() => {
      console.log("Refreshing session cookie...");
      setSessionCookie();
    }, COOKIE_EXPIRATION_TIME);

    return () => clearInterval(interval); // Cleanup interval on unmount
  }, []);

  return (
    <div className="h-screen flex flex-col overflow-hidden">
      <Header />
      <div className="flex flex-1 basis-0">
        <Sidebar />
        <Route path="/:listId?">
          {showCalendar ? <CalendarView /> : <Todos />}
        </Route>
      </div>
      <Snackbar
        open={errorAlert !== ""}
        autoHideDuration={4000}
        onClose={() => setErrorAlert("")}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert
          // icon={<Check fontSize="inherit" />}
          elevation={6}
          variant="filled"
          onClose={() => setErrorAlert("")}
          severity="error"
        >
          {errorAlert}
        </Alert>
      </Snackbar>
      <Snackbar
        open={successAlert !== ""}
        autoHideDuration={4000}
        onClose={() => {
          setSuccessAlert("");
        }}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert
          // icon={<Check fontSize="inherit" />}
          elevation={6}
          variant="filled"
          //onClose={() => setSuccessAlert("")}
          severity="success"
        >
          {successAlert}
        </Alert>
      </Snackbar>
    </div>
  );
}

export default App;
