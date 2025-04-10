import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { useContext, useEffect, useState } from "react";
import { Route } from "wouter";
import CalendarView from "./components/calendar";
import Header from "./components/misc/header";
import Sidebar from "./components/misc/sidebar";
import { MainContext } from "./provider/mainProvider";
import Todos from "./components/todos/Todos";

function App() {
  const {
    errorAlert,
    setErrorAlert,
    successAlert,
    setSuccessAlert,
    showCalendar,
  } = useContext(MainContext)!;

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
