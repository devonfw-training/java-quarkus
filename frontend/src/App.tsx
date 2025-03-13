import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { useContext } from "react";
import { Route } from "wouter";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import AddTodo from "./components/Todos/AddTodo";
import Todos from "./components/Todos/Todos";
import { MainContext } from "./context/MainContext";

function App() {
  const { errorAlert, setErrorAlert, addTodo } = useContext(MainContext)!;

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
