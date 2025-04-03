import React, { useState, useEffect } from "react";
import { render } from "react-dom";
import { DeleteConfirmProvider } from "./provider/deleteConfirmProvider";
import { MainProvider } from "./provider/mainProvider";
import { ThemeProvider } from "./provider/themeProvider";
import { TodoListProvider } from "./provider/todoListProvider";
import { TodoProvider } from "./provider/todoProvider";
import "./styles/styles.css";
import * as serviceWorkerRegistration from "./worker/serviceWorkerRegistration";
import { initKeycloak } from "./keycloak";
import App from "./App";

const Root = () => {
  const [authenticated, setAuthenticated] = useState(false); // Track authentication status

  useEffect(() => {
    initKeycloak(() => {
      console.log("User authenticated, rendering App...");
      setAuthenticated(true);
    });
  }, []);

  if (!authenticated) {
    return <p>Loading authentication...</p>; // Show a loading message until authenticated
  }

  return (
    <MainProvider>
      <ThemeProvider>
        <DeleteConfirmProvider>
          <TodoProvider>
            <TodoListProvider>
              <App />
            </TodoListProvider>
          </TodoProvider>
        </DeleteConfirmProvider>
      </ThemeProvider>
    </MainProvider>
  );
};

render(<Root />, document.getElementById("root"));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://cra.link/PWA
serviceWorkerRegistration.register();
