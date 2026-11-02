import { createContext, useContext, useEffect, useState } from "react";
import { useRoute } from "wouter";
import { useLocation } from "wouter";
import { TaskListTypeI } from "../types/types";
import { MainContext } from "./mainProvider";
import { keycloak } from "../auth/keycloak";

export const TodoListContext = createContext<TodoListInterfaceI | null>(null);

export const TodoListProvider = ({ children }: PropsI) => {
    const { setErrorAlert, setSuccessAlert } = useContext(MainContext)!;

  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const [taskLists, setTaskLists] = useState<TaskListTypeI[]>([]);
  const [, navigate] = useLocation();

    const authHeaders = () => ({
        Authorization: `Bearer ${keycloak.token}`,
    });

    const jsonAuthHeaders = () => ({
        Authorization: `Bearer ${keycloak.token}`,
        "Content-Type": "application/json",
    });

    const refreshTokenIfNeeded = async () => {
        if (!keycloak.authenticated || !keycloak.token) {
            console.warn("Kein gültiges Keycloak Token vorhanden.");
            return false;
        }

        await keycloak.updateToken(30);
        return true;
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const hasValidToken = await refreshTokenIfNeeded();

                if (!hasValidToken) {
                    return;
                }

                const response = await fetch(`/api/task/lists`, {
                    method: "GET",
                    headers: authHeaders(),
                });

                if (!response.ok) {
                    console.error("Fehler beim Laden der Listen:", response.status);
                    setErrorAlert("List could not be loaded!");
                    return;
                }

                const json = await response.json();
                setTaskLists(json);
            } catch (error) {
                console.error("Error fetching data:", error);
                setErrorAlert("List could not be loaded!");
            }
        };

        fetchData();
    }, [setErrorAlert]);

    async function loadLists() {
        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(`/api/task/lists`, {
                method: "GET",
                headers: authHeaders(),
            });

            if (!response.ok) {
                console.error("Fehler beim Laden der Listen:", response.status);
                setErrorAlert("List could not be loaded!");
                return;
            }

            const json = await response.json();
            setTaskLists(json);
        } catch (error) {
            console.error(error);
            setErrorAlert("List could not be loaded!");
        }
    }

    async function generateRandomList(title: string) {
        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(`/api/task/list/multiple-random-activities`, {
                method: "POST",
                headers: {
                    ...authHeaders(),
                    Accept: "application/json",
                    "Content-Type": "text/plain",
                },
                body: title,
            });

            if (!response.ok) {
                console.error("Fehler beim Erzeugen der zufälligen Liste:", response.status);
                setErrorAlert("List could not be loaded!");
                return;
            }

            await loadLists();
        } catch (error) {
            console.error(error);
            setErrorAlert("List could not be loaded!");
        }
    }

    async function editTodoList(newTitle: string) {
        if (undefined === listId) {
            return;
        }

        const taskList = taskLists.find((taskList) => taskList.id === +listId!);

        if (!taskList) {
            return;
        }

        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const updatedTaskList = {
                ...taskList,
                title: newTitle,
            };

            const response = await fetch("/api/task/list", {
                method: "POST",
                headers: jsonAuthHeaders(),
                body: JSON.stringify(updatedTaskList),
            });

            if (!response.ok) {
                console.error("Fehler beim Bearbeiten der Todo-Liste:", response.status);
                setErrorAlert("Todo List could not be saved!");
                return;
            }

            const newVersion = await response.json();

            const updatedTaskLists = taskLists.map((existingTaskList) =>
                existingTaskList.id === updatedTaskList.id
                    ? {
                        ...updatedTaskList,
                        version: newVersion,
                    }
                    : existingTaskList
            );

            setTaskLists(updatedTaskLists);
            setSuccessAlert("Todo List edit!");
        } catch (error) {
            console.error(error);
            setErrorAlert("Todo List could not be saved!");
        }
    }

    const addTaskList = async (title: string) => {
        if (!title.trim()) {
            return;
        }

        const taskList: TaskListTypeI = {
            id: Number.NaN,
            version: 0,
            title,
        };

        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch("/api/task/list", {
                method: "POST",
                headers: jsonAuthHeaders(),
                body: JSON.stringify(taskList),
            });

            if (!response.ok) {
                console.error("Fehler beim Erstellen der Liste:", response.status);
                setErrorAlert("List could not be created!");
                return;
            }

            const newId = await response.json();

            taskList.id = newId;
            const newTaskLists = [taskList, ...taskLists];

            setTaskLists(newTaskLists);
            setSuccessAlert("Todo List created!");
        } catch (error) {
            console.error(error);
            setErrorAlert("List could not be created!");
        }
    };

    const delTaskList = async (id: number) => {
        try {
            const hasValidToken = await refreshTokenIfNeeded();

            if (!hasValidToken) {
                return;
            }

            const response = await fetch(`/api/task/list/${encodeURIComponent(id)}`, {
                method: "DELETE",
                headers: authHeaders(),
            });

            if (response.status === 403) {
                setErrorAlert("List could not be deleted (No permissions)!");
                return;
            }

            if (!response.ok) {
                console.error("Fehler beim Löschen der Liste:", response.status);
                setErrorAlert("List could not be deleted!");
                return;
            }

            setTaskLists(taskLists.filter((taskList) => taskList.id !== id));

            if (undefined !== listId && id === +listId) {
                navigate("/");
            }

            setSuccessAlert("Todo List deleted!");
        } catch (error) {
            console.error(error);
            setErrorAlert("List could not be deleted!");
        }
    };

    return (
        <TodoListContext.Provider
            value={{
                taskLists,
                setTaskLists,
                editTodoList,
                addTaskList,
                delTaskList,
                generateRandomList,
            }}
        >
            {children}
        </TodoListContext.Provider>
    );
};
