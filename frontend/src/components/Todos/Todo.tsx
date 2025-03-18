import { CalendarDays, Edit2, Star, StarOff, Trash } from "lucide-react";
import { forwardRef, RefObject, useContext, useRef, useState } from "react";
import useShowOnHover from "../../hooks/showOnHover";
import { DeleteConfirmContext } from "../../provider/deleteConfirmProvider";
import { TodoContext } from "../../provider/todoProvider";
import { TaskItemTypeI } from "../../types/types";
import { DeleteConfirm } from "../dialogs/deleteConfirm";
import EditConfirm from "../dialogs/editConfirm";
import Checkbox from "../utils/checkbox";

interface TodoI {
  todo: TaskItemTypeI;
  onDelete: () => void;
  onEdit: () => void;
}

const Todo = forwardRef(({ todo, onDelete, onEdit }: TodoI, ref: any) => {
  const { markComplete, delTodo, editTodo, markStar } =
    useContext(TodoContext)!;
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const { isDeleteConfirmation } = useContext(DeleteConfirmContext)!;

  const todoContainerRef: RefObject<HTMLDivElement> = useRef(null);
  const todoOptionsRef: RefObject<HTMLDivElement> = useRef(null);
  useShowOnHover(todoContainerRef, todoOptionsRef);

  const deleteTodo = (e: any) => {
    if (e.shiftKey || isDeleteConfirmation) {
      delTodo(todo.id);
      onDelete();
    } else setDeleteOpen(true);
  };

  return (
    <div ref={ref}>
      <div
        ref={todoContainerRef}
        className={`p-4 flex flex-row gap-2.5 justify-between items-center border-2 rounded-lg hover:border-primary ${
          todo.starred
            ? "border-light-primary"
            : "dark:border-black border-light-gray"
        }`}
      >
        <div className="flex flex-row gap-6 items-center">
          <Checkbox
            state={todo.completed}
            onClick={() => markComplete(todo.id)}
          />
          <div className="flex flex-col gap-1 justify-center">
            <p className="dark:text-white text-black break-all">{todo.title}</p>
            <div
              className={`flex flex-row items-center gap-1 ${
                todo.deadline ? "" : "hidden"
              }`}
            >
              <CalendarDays className="w-4 h-4 dark:text-white text-black" />
              <p className="text-xs dark:text-white text-black">
                {new Date(todo.deadline ?? "").toLocaleString()}
              </p>
            </div>
          </div>
        </div>
        <div ref={todoOptionsRef} className="flex-row hidden gap-2.5">
          <Edit2
            className="text-black dark:text-white cursor-pointer w-5 h-5"
            onClick={() => setEditOpen(true)}
          />
          <span onClick={() => markStar(todo.id)}>
            {!todo.starred ? (
              <Star className="dark:text-light-primary text-primary cursor-pointer w-5 h-5" />
            ) : (
              <StarOff className="dark:text-light-primary text-primary cursor-pointer w-5 h-5" />
            )}
          </span>
          <Trash
            className="text-red cursor-pointer w-5 h-5"
            onClick={(e) => deleteTodo(e)}
          />
        </div>
      </div>
      <DeleteConfirm
        yes={() => {
          setDeleteOpen(false);
          setTimeout(() => {
            delTodo(todo.id);
            onDelete();
          }, 200);
        }}
        open={deleteOpen}
        close={() => setDeleteOpen(false)}
      />
      <EditConfirm
        yes={(newTitle: string, newDeadline?: string) => {
          setEditOpen(false);
          setTimeout(() => {
            editTodo(todo.id, newTitle, newDeadline);
            onEdit();
          }, 200);
        }}
        open={editOpen}
        close={() => setEditOpen(false)}
        title={todo.title}
        deadline={todo.deadline?.substring(0, 16)}
      />
    </div>
  );
});

export default Todo;
