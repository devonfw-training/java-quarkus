import { createContext, ReactNode, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { TaskItemType, TaskListType } from "../types/types";

interface MainContextInterface {
  errorAlert: string;
  todos: TaskItemType[];
  taskLists: TaskListType[];
  showSettings: boolean;
  showAbout: boolean;
  showCalendar: boolean;
  setErrorAlert: React.Dispatch<React.SetStateAction<string>>;
  setTodos: React.Dispatch<React.SetStateAction<TaskItemType[]>>;
  setTaskLists: React.Dispatch<React.SetStateAction<TaskListType[]>>;
  changeShowSettings: () => void;
  setShowAbout: React.Dispatch<React.SetStateAction<boolean>>;
  changeShowCalendar: () => void;
  markComplete: (id: number) => void;
  delTodo: (id: number) => void;
  deleteAll: () => void;
  editTodo: (id: number, text: string) => void;
  addTodo: (title: string) => void;
  moveTodo: (old: number, new_: number) => void;
  markStar: (id: number) => void;
}

interface Props {
  children: ReactNode;
}

export const MainContext = createContext<MainContextInterface | null>(null);

export const MainProvider = ({ children }: Props) => {
  const [errorAlert, setErrorAlert] = useState("");
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [todos, setTodos] = useState<TaskItemType[]>([]);
  const [taskLists, setTaskLists] = useState<TaskListType[]>([]);
  const [showSettings, setShowSettings] = useState<boolean>(false);
  const [showAbout, setShowAbout] = useState<boolean>(false);
  const [showCalendar, setShowCalendar] = useState<boolean>(false);

  useEffect(() => {
    if (undefined !== listId) {
      fetch(`/api/task/list-with-items/${encodeURIComponent(+listId)}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => response.json())
        .then((json) => setTodos(json.items))
        .catch((error) => {
          console.error(error);
          setErrorAlert("Items could not be loaded!");
        });
    }
  }, [listId]);

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
  }, []);

  const saveTaskItem = (
    taskItem: TaskItemType,
    onSuccess: (value: number) => any
  ) => {
    // Send data to the backend via POST
    fetch("/api/task/item", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(taskItem), // body data type must match "Content-Type" header
    })
      .then((response) => response.json())
      .then(onSuccess)
      .catch((error) => {
        console.error(error);
        setErrorAlert("Item could not be saved!");
      });
  };

  const addTodo = (title: string) => {
    if (title.trim()) {
      const taskItem: TaskItemType = {
        id: Number.NaN,
        title,
        completed: false,
        starred: false,
        taskListId: +listId!,
      };
      console.log(JSON.stringify(taskItem));
      saveTaskItem(taskItem, (newId) => {
        taskItem.id = newId;
        const orderTodos = [taskItem, ...todos];
        orderStarAndComplete(orderTodos);
        setTodos(orderTodos);
      });
    }
  };
  const editTodo: (id: number, text: string) => void = (
    id: number,
    text: string
  ) => {
    if (!(text === null) && text.trim()) {
      const taskItem = todos.find((todo) => todo.id === id);
      if (taskItem) {
        taskItem.title = text;
        saveTaskItem(taskItem, () =>
          setTodos(
            todos.map((todo) => {
              if (todo.id === id) {
                todo = taskItem;
              }
              return todo;
            })
          )
        );
      }
    }
  };
  const markComplete = (id: number) => {
    const taskItem = todos.find((todo) => todo.id === id);
    if (taskItem) {
      taskItem.completed = !taskItem.completed;
      saveTaskItem(taskItem, () => {
        const orderTodos = todos.map((todo) =>
          todo.id === id ? taskItem : todo
        );
        orderStarAndComplete(orderTodos);
        setTodos(orderTodos);
      });
    }
  };

  const markStar = (id: number) => {
    const taskItem = todos.find((todo) => todo.id === id);
    if (taskItem) {
      taskItem.starred = !taskItem.starred;
      saveTaskItem(taskItem, () => {
        const orderTodos = todos.map((todo) =>
          todo.id === id ? taskItem : todo
        );
        orderStarAndComplete(orderTodos);
        setTodos(orderTodos);
      });
    }
  };

  const orderStarAndComplete = (todos: TaskItemType[]) => {
    todos.sort((x, y) => y.starred - x.starred);
    todos.sort((x, y) => x.completed - y.completed);
  };

  const delTodo = (id: number) => {
    fetch(`/api/task/item/${encodeURIComponent(id)}`, {
      method: "DELETE",
    })
      .then(() => setTodos(todos.filter((todo) => todo.id !== id)))
      .catch((error) => {
        console.error(error);
        setErrorAlert("Item could not be deleted!");
      });
  };

  const deleteAll = () => setTodos([]);
  const moveTodo = (old: number, new_: number) => {
    const copy = JSON.parse(JSON.stringify(todos));
    const thing = JSON.parse(JSON.stringify(todos[old]));
    copy.splice(old, 1);
    copy.splice(new_, 0, thing);
    setTodos(copy);
  };

  const changeShowSettings = () => {
    setShowSettings(!showSettings);
  };

  const changeShowCalendar = () => {
    setShowCalendar(!showCalendar);
  };

  const mainContextValue: MainContextInterface = {
    errorAlert,
    todos,
    taskLists,
    showSettings,
    showAbout,
    showCalendar,
    setErrorAlert,
    setTodos,
    setTaskLists,
    changeShowSettings,
    setShowAbout,
    changeShowCalendar,
    markComplete,
    delTodo,
    deleteAll,
    editTodo,
    addTodo,
    moveTodo,
    markStar,
  };

  return (
    <MainContext.Provider value={mainContextValue}>
      {children}
    </MainContext.Provider>
  );
};
