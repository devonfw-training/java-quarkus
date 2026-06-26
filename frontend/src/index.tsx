import { createRoot } from "react-dom/client";
import App from "./App";
import { DeleteConfirmProvider } from "./provider/deleteConfirmProvider";
import { MainProvider } from "./provider/mainProvider";
import { ThemeProvider } from "./provider/themeProvider";
import { TodoListProvider } from "./provider/todoListProvider";
import { TodoProvider } from "./provider/todoProvider";
import "./styles/styles.css";
import * as serviceWorkerRegistration from "./worker/serviceWorkerRegistration";

const rootElement = document.getElementById("root");

if (!rootElement) {
    throw new Error("Root element with id 'root' not found");
}

createRoot(rootElement).render(
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

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers
serviceWorkerRegistration.register();
