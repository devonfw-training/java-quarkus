import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { SelectedFilterE } from "../components/menus/filterMenu";
import {
  SelectedSortE,
  SelectedSortOrderE,
} from "../components/menus/sortMenu";
import { TaskItemTypeI } from "../types/types";
import { MainContext } from "./mainProvider";
import { addHeaders } from "../auth/authUtils";

export const TodoContext = createContext<TodoInterfaceI | null>(null);

export const TodoProvider = ({ children }: PropsI) => {
  const { setErrorAlert, setSuccessAlert } = useContext(MainContext)!;

  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [todos, setTodos] = useState<TaskItemTypeI[]>([]);

  useEffect(() => {
    if (undefined === listId) {
      setTodos([]);
      return;
    }

    fetch(`/api/task/list-with-items/${encodeURIComponent(+listId!)}`, {
      method: "GET",
        headers: addHeaders(),
    })
      .then((response) => response.json())
      .then((json) => setTodos(json.items))
      .catch((error) => {
        console.error(error);
        setErrorAlert("Items could not be loaded!");
      });
  }, [listId, setErrorAlert]);

  const saveTaskItem = (
    taskItem: TaskItemTypeI,
    onSuccess: (value: number) => any
  ) => {
    // Send data to the backend via POST
    fetch("/api/task/item", {
      method: "POST",
      headers: addHeaders(),
      body: JSON.stringify(taskItem), // body data type must match "Content-Type" header
    })
      .then((response) => response.json())
      .then(onSuccess)
      .catch((error) => {
        console.error(error);
        setErrorAlert("Item could not be saved!");
      });
  };

  const addTodo = (title: string, deadline: string | null) => {
    if (undefined === listId) {
      return;
    }

    if (title.trim()) {
      const taskItem: TaskItemTypeI = {
        id: Number.NaN,
        title,
        version: 0,
        completed: false,
        starred: false,
        taskListId: +listId!,
        deadline: 0 === (deadline?.length ?? 0) ? null : deadline,
      };

      saveTaskItem(taskItem, (newId) => {
        taskItem.id = newId;
        const orderTodos = [taskItem, ...todos];
        setTodos(orderTodos);
        setSuccessAlert("Item created!");
      });
    }
  };

  const editTodo: (
    id: number,
    text: string,
    deadline: string | null
  ) => void = (id: number, text: string, deadline: string | null) => {
    if (!(text === null) && text.trim()) {
      const taskItem = todos.find((todo) => todo.id === id);
      if (taskItem) {
        taskItem.title = text;
        taskItem.deadline = deadline;
        saveTaskItem(taskItem, (newVersion) => {
          taskItem.version = newVersion;
          setTodos(
            todos.map((todo) => {
              if (todo.id === id) {
                todo = taskItem;
              }
              return todo;
            })
          );
          setSuccessAlert("Item edited!");
        });
      }
    }
  };

  const markComplete = (id: number) => {
    const taskItem = todos.find((todo) => todo.id === id);
    if (taskItem) {
      taskItem.completed = !taskItem.completed;
      saveTaskItem(taskItem, (newVersion) => {
        taskItem.version = newVersion;
        const orderTodos = todos.map((todo) =>
          todo.id === id ? taskItem : todo
        );
        setTodos(orderTodos);
        setSuccessAlert("Item marked as completed!");
      });
    }
  };

  const markStar = (id: number) => {
    const taskItem = todos.find((todo) => todo.id === id);
    if (taskItem) {
      taskItem.starred = !taskItem.starred;
      saveTaskItem(taskItem, (newVersion) => {
        taskItem.version = newVersion;
        const orderTodos = todos.map((todo) =>
          todo.id === id ? taskItem : todo
        );
        setTodos(orderTodos);
        setSuccessAlert(`Item ${taskItem.starred ? "" : "un"}starred!`);
      });
    }
  };

  const delTodo = (id: number) => {
    fetch(`/api/task/item/${encodeURIComponent(id)}`, {
      method: "DELETE",
      headers: addHeaders(),
    })
      .then((res) => {
        if(res.status === 403){
          setErrorAlert("Item could not be deleted (No permissions)!");
          return;
        }
        setTodos(todos.filter((todo) => todo.id !== id));
        setSuccessAlert("Item deleted!");
      })
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

  const applyFilter = (todos: TaskItemTypeI[], filter: SelectedFilterE) => {
    const sortedTodos = [...todos];

    switch (filter) {
      case SelectedFilterE.NONE:
        return sortedTodos;
      case SelectedFilterE.WITH_DUE_DATE:
        return sortedTodos.filter((e) => e.deadline != null);
      case SelectedFilterE.WITHOUT_DUE_DATE:
        return sortedTodos.filter((e) => e.deadline == null);
      case SelectedFilterE.ONLY_STARRED:
        return sortedTodos.filter((e) => e.starred);
      case SelectedFilterE.NOT_STARRED:
        return sortedTodos.filter((e) => !e.starred);
    }
  };

  const applySort = (
    todos: TaskItemTypeI[],
    sort: {
      selectedSort: SelectedSortE;
      selectedSortOrder: SelectedSortOrderE;
    }
  ) => {
    const sortedTodos = [...todos];

    if (
      sort.selectedSort === SelectedSortE.NONE ||
      sort.selectedSortOrder === SelectedSortOrderE.NOT_SELECTED
    ) {
      return todos;
    }

    switch (sort.selectedSort) {
      case SelectedSortE.BY_CREATION_DATE:
        if (sort.selectedSortOrder === SelectedSortOrderE.ASC) {
          return sortedTodos.sort((a, b) => a.id - b.id);
        } else {
          return sortedTodos.sort((a, b) => b.id - a.id);
        }
      case SelectedSortE.ALPHABETICALLY:
        if (sort.selectedSortOrder === SelectedSortOrderE.ASC) {
          return sortedTodos.sort((a, b) => a.title.localeCompare(b.title));
        } else {
          return sortedTodos.sort((a, b) => b.title.localeCompare(a.title));
        }
      case SelectedSortE.BY_DUE_DATE:
        const sortedDateTodos = [];
        if (sort.selectedSortOrder === SelectedSortOrderE.ASC) {
          sortedDateTodos.push(
            ...sortedTodos
              .filter((e) => null != e.deadline)
              .sort((a, b) => {
                console.log(a);
                console.log(b);

                return (
                  new Date(a.deadline!).getTime() -
                  new Date(b.deadline!).getTime()
                );
              })
          );
        } else {
          sortedDateTodos.push(
            ...sortedTodos
              .filter((e) => null != e.deadline)
              .sort((a, b) => {
                return (
                  new Date(b.deadline!).getTime() -
                  new Date(a.deadline!).getTime()
                );
              })
          );
        }
        sortedDateTodos.push(...sortedTodos.filter((e) => null == e.deadline));
        return sortedDateTodos;
      case SelectedSortE.STARRED:
        if (sort.selectedSortOrder === SelectedSortOrderE.ASC) {
          return sortedTodos.sort(
            (a, b) => Number(b.starred) - Number(a.starred)
          );
        } else {
          return sortedTodos.sort(
            (a, b) => Number(a.starred) - Number(b.starred)
          );
        }
    }
  };

  return (
    <TodoContext.Provider
      value={{
        todos,

        setTodos,
        markComplete,
        delTodo,
        deleteAll,
        editTodo,
        addTodo,
        moveTodo,
        markStar,
        applyFilter,
        applySort,
      }}
    >
      {children}
    </TodoContext.Provider>
  );
};
