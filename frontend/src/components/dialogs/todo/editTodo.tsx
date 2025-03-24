import { X } from "lucide-react";
import { useState } from "react";
import DialogBase from "../dialogBase";

interface EditTodoI {
  yes: (newTitle: string, newDeadline: string | null) => void;
  open: boolean;
  close: () => void;
  title: string;
  deadline: string | null;
}

const EditTodo = ({ open, close, title, deadline, yes }: EditTodoI) => {
  const [newTitle, setNewTitle] = useState(title);
  const [newDeadline, setNewDeadline] = useState(deadline);
  const onClose = () => {
    setNewTitle(title);
    setNewDeadline(deadline);
    close();
  };

  return (
    <DialogBase show={open} close={onClose}>
      <div className="flex flex-col dark:bg-black bg-white border-primary border-2 rounded-lg p-6 w-[500px]">
        <div className="flex flex-row justify-between">
          <h1 className="text-4xl dark:text-white text-black font-bold">
            Todo
          </h1>
          <X
            className="dark:text-white text-black w-11 h-11 cursor-pointer"
            onClick={onClose}
          />
        </div>
        <span className="dark:text-white text-black mt-6">Title</span>
        <input
          type="text"
          className="dark:text-white text-black border-2 dark:border-white border-black p-4 rounded-lg mt-1"
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
        />
        <span className="dark:text-white text-black mt-6">Date (optional)</span>
        <input
          type="datetime-local"
          className="dark:text-white text-black border-2 dark:border-white border-black p-4 rounded-lg mt-1"
          value={newDeadline ?? ""}
          onChange={(e) => {
            setNewDeadline(0 === e.target.value.length ? null : e.target.value);
          }}
        />
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() => newTitle.trim() && yes(newTitle, newDeadline)}
        >
          Save Todo
        </button>
      </div>
    </DialogBase>
  );
};

export default EditTodo;
