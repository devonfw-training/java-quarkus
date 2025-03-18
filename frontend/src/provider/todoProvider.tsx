import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { TaskItemType, TaskListType } from "../types/types";
import { MainContext } from "./mainProvider";

export const TodoContext = createContext<TodoInterface | null>(null);

export const TodoProvider = ({ children }: Props) => {
  const { setErrorAlert } = useContext(MainContext)!;

  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [todos, setTodos] = useState<TaskItemType[]>([]);
  const [taskLists, setTaskLists] = useState<TaskListType[]>([]);

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
  }, [listId, setErrorAlert]);

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
        orderStar(orderTodos);
        setTodos(orderTodos);
      });
    }
  };

  const editTodo: (id: number, text: string, deadline?: string) => void = (
    id: number,
    text: string,
    deadline?: string
  ) => {
    if (!(text === null) && text.trim()) {
      const taskItem = todos.find((todo) => todo.id === id);
      if (taskItem) {
        taskItem.title = text;
        taskItem.deadline = deadline;
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
        orderStar(orderTodos);
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
        orderStar(orderTodos);
        setTodos(orderTodos);
      });
    }
  };

  const orderStar = (todos: TaskItemType[]) => {
    todos.sort((x, y) => y.starred - x.starred);
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

  return (
    <TodoContext.Provider
      value={{
        todos,
        taskLists,

        setTodos,
        setTaskLists,
        markComplete,
        delTodo,
        deleteAll,
        editTodo,
        addTodo,
        moveTodo,
        markStar,
      }}
    >
      {children}
    </TodoContext.Provider>
  );
};
