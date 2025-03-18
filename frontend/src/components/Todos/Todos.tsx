import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { Edit2, Filter, ListFilter, Plus } from "lucide-react";
import { useContext, useRef, useState } from "react";
import FlipMove from "react-flip-move";
import { useRoute } from "wouter";
import useHasOverflow from "../../hooks/hasOverflow";
import useScrollbarWidth from "../../hooks/scrollBarWidth";
import { TodoContext } from "../../provider/todoProvider";
import { TaskItemTypeI } from "../../types/types";
import Todo from "./todo";

const Todos = () => {
  const { todos, taskLists } = useContext(TodoContext)!;
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;

  const [deleteSnackOpen, setDeleteSnackOpen] = useState(false);
  const [editSnackOpen, setEditSnackOpen] = useState(false);

  return (
    <div className="dark:bg-light-black w-full p-12 flex flex-col">
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row gap-2.5 items-baseline">
          <h1 className="text-3xl dark:text-white">
            {taskLists.filter((e) => listId && e.id === +listId)[0]?.title ??
              "Loading..."}
          </h1>
          <Edit2 className="cursor-pointer dark:text-white" />
        </div>
        <Plus className="cursor-pointer dark:text-light-primary text-primary w-10 h-10" />
      </div>
      <div className="mt-6 flex flex-row gap-6 w-full grow">
        <List title="Tasks" todos={todos.filter((e) => !e.completed)} />
        <List title="Done" todos={todos.filter((e) => e.completed)} />
      </div>

      <Snackbar
        open={deleteSnackOpen}
        autoHideDuration={4000}
        onClose={() => setDeleteSnackOpen(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert
          elevation={6}
          variant="filled"
          onClose={() => setDeleteSnackOpen(false)}
          severity="success"
        >
          Successfully deleted item!
        </Alert>
      </Snackbar>
      <Snackbar
        open={editSnackOpen}
        autoHideDuration={4000}
        onClose={() => setEditSnackOpen(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert
          elevation={6}
          variant="filled"
          onClose={() => setEditSnackOpen(false)}
          severity="success"
        >
          Successfully edited item!
        </Alert>
      </Snackbar>
    </div>
  );
};

interface ListI {
  title: string;
  todos: TaskItemTypeI[];
}

function List({ title, todos }: ListI) {
  const scrollContainerRef = useRef(null);
  const scrollbarWidth = useScrollbarWidth();
  const hasOverflow = useHasOverflow(scrollContainerRef);

  const [, setDeleteSnackOpen] = useState(false);
  const [, setEditSnackOpen] = useState(false);

  return (
    <div className="flex flex-col w-full">
      <div className="flex flex-row justify-between items-center">
        <p className="dark:text-white text-black">{`${title} - ${todos.length}`}</p>
        <div
          className={`flex flex-row gap-2.5 transition-all `}
          style={{
            marginRight: `${
              hasOverflow
                ? `calc(calc(var(--spacing) * 2.5) + ${scrollbarWidth}px)`
                : ""
            }`,
          }}
        >
          <Filter className="text-light-primary cursor-pointer" />
          <ListFilter className="text-light-primary cursor-pointer" />
        </div>
      </div>
      <div
        ref={scrollContainerRef}
        className={`mt-2.5 overflow-y-auto flex-1 basis-0 ${
          hasOverflow ? "pr-2.5" : ""
        }`}
      >
        <div>
          <FlipMove className="gap-2.5 flex flex-col">
            {todos.map((todo, i) => {
              return (
                <Todo
                  todo={todo}
                  key={todo.id}
                  onDelete={() => setDeleteSnackOpen(true)}
                  onEdit={() => setEditSnackOpen(true)}
                />
              );
            })}
          </FlipMove>
        </div>
      </div>
    </div>
  );
}

export default Todos;
