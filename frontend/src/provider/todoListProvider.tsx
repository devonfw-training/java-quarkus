import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { navigate } from "wouter/use-location";
import { TaskListTypeI } from "../types/types";
import { MainContext } from "./mainProvider";

export const TodoListContext = createContext<TodoListInterfaceI | null>(null);

export const TodoListProvider = ({ children }: PropsI) => {
  const { setErrorAlert, setSuccessAlert } = useContext(MainContext)!;

  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [taskLists, setTaskLists] = useState<TaskListTypeI[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/task/lists`, {
          method: 'GET',
          credentials: 'include',
        });
        if (response.status === 401) {
          const data = await response.json();
          window.location.href = data.redirectUrl;  // Redirect to Keycloak
        } else {
          const json = await response.json();
          setTaskLists(json);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        setErrorAlert("List could not be loaded!"); // Set error alert in case of failure
      }
    };
    fetchData();
  }, [setErrorAlert]);

  function editTodoList(newTitle: string) {
    if (undefined === listId) {
      return;
    }

    const taskList = taskLists.find((taskList) => taskList.id === +listId!);

    if (taskList) {
      taskList.title = newTitle;

      fetch("/api/task/list", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json', // Ensure that the correct content type is set
        },
        body: JSON.stringify(taskList),
      })
        .then((response) => response.json())
        .then((newVersion) => {
          taskList.version = newVersion;
          setTaskLists(taskLists);
          setSuccessAlert("Todo List edit!");
        })
        .catch((error) => {
          console.error(error);
          setErrorAlert("Todo List could not be saved!");
        });
    }
  }

  const addTaskList = (title: string) => {
    if (title.trim()) {
      const taskList: TaskListTypeI = {
        id: Number.NaN,
        version: 0,
        title,
      };

      fetch("/api/task/list", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json', // Ensure that the correct content type is set
        },
        body: JSON.stringify(taskList), // body data type must match "Content-Type" header
      })
        .then((response) => response.json())
        .then((newId) => {
          taskList.id = newId;
          const newTaskLists = [taskList, ...taskLists];
          setTaskLists(newTaskLists);
          setSuccessAlert("Todo List created!");
        })
        .catch((error) => {
          console.error(error);
          setErrorAlert("List could not be created!");
        });
    }
  };

  const delTaskList = (id: number) => {
    fetch(`/api/task/list/${encodeURIComponent(id)}`, {
      method: "DELETE",
    })
      .then((res) => {
        if(res.status === 403){
          setErrorAlert("List could not be deleted (No permissions)!");
          return;
        }
        setTaskLists(taskLists.filter((taskList) => taskList.id !== id));
        if (undefined !== listId && id === +listId) {
          navigate("/");
        }
        setSuccessAlert("Todo List deleted!");
      })
      .catch((error) => {
        console.error(error);
        setErrorAlert("List could not be deleted!");
      });
  };

  return (
    <TodoListContext.Provider
      value={{
        taskLists,

        setTaskLists,
        editTodoList,
        addTaskList,
        delTaskList,
      }}
    >
      {children}
    </TodoListContext.Provider>
  );
};
