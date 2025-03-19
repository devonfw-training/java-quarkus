import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { TaskListTypeI } from "../types/types";
import { MainContext } from "./mainProvider";

export const TodoListContext = createContext<TodoListInterfaceI | null>(null);

export const TodoListProvider = ({ children }: PropsI) => {
  const { setErrorAlert } = useContext(MainContext)!;

  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [taskLists, setTaskLists] = useState<TaskListTypeI[]>([]);

  useEffect(() => {
    fetch(`/api/task/lists`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((json) => setTaskLists(json))
      .catch((error) => {
        console.error(error);
        setErrorAlert("List could not be loaded!");
      });
  }, [setErrorAlert]);

  return (
    <TodoListContext.Provider
      value={{
        taskLists,

        setTaskLists,
      }}
    >
      {children}
    </TodoListContext.Provider>
  );
};
