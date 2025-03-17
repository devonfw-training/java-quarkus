import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { useContext } from "react";
import { Route } from "wouter";
import Header from "./components/misc/header";
import Sidebar from "./components/misc/sidebar";
import AddTodo from "./components/todos/addTodo";
import Todos from "./components/todos/todos";
import { MainContext } from "./provider/mainProvider";
import { TodoContext } from "./provider/todoProvider";

function App() {
  const { errorAlert, setErrorAlert } = useContext(MainContext)!;
  const { addTodo } = useContext(TodoContext)!;

  return (
    <div className="h-screen flex flex-col overflow-hidden">
      <Header />
      <div className="flex flex-1 basis-0">
        <Sidebar />
        <Route path="/:listId?">
          <AddTodo addTodo={addTodo} />
          <Todos />
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
    </div>
  );
}

export default App;
