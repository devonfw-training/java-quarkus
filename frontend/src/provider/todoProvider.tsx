import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { SelectedFilterE } from "../components/menus/filterMenu";
import {
    SelectedSortE,
    SelectedSortOrderE,
} from "../components/menus/sortMenu";
import { TaskItemTypeI } from "../types/types";
import { MainContext } from "./mainProvider";
import { keycloak } from "../auth/keycloak";

export const TodoContext = createContext<TodoInterfaceI | null>(null);

export const TodoProvider = ({ children }: PropsI) => {
    const { setErrorAlert, setSuccessAlert } = useContext(MainContext)!;

    const [, params] = useRoute("/:listId");
    const listId = params?.listId;

    const [todos, setTodos] = useState<TaskItemTypeI[]>([]);

    const getNumericListId = (): number | null => {
        if (!listId) {
            return null;
        }

        const numericListId = Number(listId);

        if (Number.isNaN(numericListId)) {
            return null;
        }

        return numericListId;
    };

    const refreshTokenIfNeeded = async (): Promise<boolean> => {
        if (!keycloak.authenticated || !keycloak.token) {
            console.warn("Kein gültiges Keycloak Token vorhanden.");
            return false;
        }

        await keycloak.updateToken(30);
        return true;
    };

    const authHeaders = () => ({
        Authorization: `Bearer ${keycloak.token}`,
    });

    const jsonAuthHeaders = () => ({
        Authorization: `Bearer ${keycloak.token}`,
        Accept: "application/json",
        "Content-Type": "application/json",
    });

    useEffect(() => {
        const fetchItems = async () => {
            const numericListId = getNumericListId();

            if (numericListId === null) {
                setTodos([]);
                return;
            }

            try {
                const hasValidToken = await refreshTokenIfNeeded();

                if (!hasValidToken) {
                    return;
                }

                const response = await fetch(
                    `/api/task/list-with-items/${encodeURIComponent(numericListId)}`,
                    {
                        method: "GET",
                        headers: authHeaders(),
                    }
                );

                if (!response.ok) {
                    console.error("Items could not be loaded:", response.status);
                    setErrorAlert("Items could not be loaded!");
                    return;
                }

                const json = await response.json();
                setTodos(json.items ?? []);
            } catch (error) {
                console.error(error);
                setErrorAlert("Items could not be loaded!");
            }
        };

        fetchItems();
    }, [listId, setErrorAlert]);

    const loadItems = async () => {
        const numericListId = getNumericListId();

        if (numericListId === null) {
            setTodos([]);
            return;
        }

        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(
                `/api/task/list-with-items/${encodeURIComponent(numericListId)}`,
                {
                    method: "GET",
                    headers: authHeaders(),
                }
            );

            if (!response.ok) {
                console.error("Items could not be loaded:", response.status);
                setErrorAlert("Items could not be loaded!");
                return;
            }

            const json = await response.json();
            setTodos(json.items ?? []);
        } catch (error) {
            console.error(error);
            setErrorAlert("Items could not be loaded!");
        }
    };

    const saveTaskItem = async (
        taskItem: TaskItemTypeI,
        onSuccess: (value: number) => any
    ) => {
        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch("/api/task/item", {
                method: "POST",
                headers: jsonAuthHeaders(),
                body: JSON.stringify(taskItem),
            });

            if (!response.ok) {
                console.error("Item could not be saved:", response.status);
                setErrorAlert("Item could not be saved!");
                return;
            }

            const result = await response.json();
            onSuccess(result);
        } catch (error) {
            console.error(error);
            setErrorAlert("Item could not be saved!");
        }
    };

    const addRandomTodo = async () => {
        const numericListId = getNumericListId();

        if (numericListId === null) {
            return;
        }

        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(
                `/api/task/list/${encodeURIComponent(numericListId)}/random-activity`,
                {
                    method: "POST",
                    headers: {
                        ...authHeaders(),
                        Accept: "application/json",
                        "Content-Type": "application/json",
                    },
                }
            );

            if (!response.ok) {
                console.error("Random item could not be created:", response.status);
                setErrorAlert("Item could not be saved!");
                return;
            }

            await loadItems();
        } catch (error) {
            console.error(error);
            setErrorAlert("Item could not be saved!");
        }
    };

    const addTodo = (title: string, deadline: string | null) => {
        const numericListId = getNumericListId();

        if (numericListId === null) {
            return;
        }

        if (title.trim()) {
            const taskItem: TaskItemTypeI = {
                id: Number.NaN,
                title,
                version: 0,
                completed: false,
                starred: false,
                taskListId: numericListId,
                deadline: 0 === (deadline?.length ?? 0) ? null : deadline,
            };

            saveTaskItem(taskItem, (newId) => {
                taskItem.id = newId;
                const orderedTodos = [taskItem, ...todos];
                setTodos(orderedTodos);
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
                const updatedTaskItem = {
                    ...taskItem,
                    title: text,
                    deadline,
                };

                saveTaskItem(updatedTaskItem, (newVersion) => {
                    const updatedTaskItemWithVersion = {
                        ...updatedTaskItem,
                        version: newVersion,
                    };

                    setTodos(
                        todos.map((todo) =>
                            todo.id === id ? updatedTaskItemWithVersion : todo
                        )
                    );

                    setSuccessAlert("Item edited!");
                });
            }
        }
    };

    const markComplete = (id: number) => {
        const taskItem = todos.find((todo) => todo.id === id);

        if (taskItem) {
            const updatedTaskItem = {
                ...taskItem,
                completed: !taskItem.completed,
            };

            saveTaskItem(updatedTaskItem, (newVersion) => {
                const updatedTaskItemWithVersion = {
                    ...updatedTaskItem,
                    version: newVersion,
                };

                const orderedTodos = todos.map((todo) =>
                    todo.id === id ? updatedTaskItemWithVersion : todo
                );

                setTodos(orderedTodos);
                setSuccessAlert("Item marked as completed!");
            });
        }
    };

    const markStar = (id: number) => {
        const taskItem = todos.find((todo) => todo.id === id);

        if (taskItem) {
            const updatedTaskItem = {
                ...taskItem,
                starred: !taskItem.starred,
            };

            saveTaskItem(updatedTaskItem, (newVersion) => {
                const updatedTaskItemWithVersion = {
                    ...updatedTaskItem,
                    version: newVersion,
                };

                const orderedTodos = todos.map((todo) =>
                    todo.id === id ? updatedTaskItemWithVersion : todo
                );

                setTodos(orderedTodos);
                setSuccessAlert(`Item ${updatedTaskItem.starred ? "" : "un"}starred!`);
            });
        }
    };

    const delTodo = async (id: number) => {
        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(`/api/task/item/${encodeURIComponent(id)}`, {
                method: "DELETE",
                headers: authHeaders(),
            });

            if (response.status === 403) {
                setErrorAlert("Item could not be deleted (No permissions)!");
                return;
            }

            if (!response.ok) {
                console.error("Item could not be deleted:", response.status);
                setErrorAlert("Item could not be deleted!");
                return;
            }

            setTodos(todos.filter((todo) => todo.id !== id));
            setSuccessAlert("Item deleted!");
        } catch (error) {
            console.error(error);
            setErrorAlert("Item could not be deleted!");
        }
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
                const sortedDateTodos: TaskItemTypeI[] = [];

                if (sort.selectedSortOrder === SelectedSortOrderE.ASC) {
                    sortedDateTodos.push(
                        ...sortedTodos
                            .filter((e) => null != e.deadline)
                            .sort((a, b) => {
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
                addRandomTodo,
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