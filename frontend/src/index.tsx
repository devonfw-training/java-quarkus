import { render } from "react-dom";
import App from "./app";
import { DeleteConfirmProvider } from "./provider/deleteConfirmProvider";
import { MainProvider } from "./provider/mainProvider";
import { ThemeProvider } from "./provider/themeProvider";
import { TodoListProvider } from "./provider/todoListProvider";
import { TodoProvider } from "./provider/todoProvider";
import "./styles/styles.css";
import * as serviceWorkerRegistration from "./worker/serviceWorkerRegistration";

render(
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
  </MainProvider>,
  document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://cra.link/PWA
serviceWorkerRegistration.register();
