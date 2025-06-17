import { X } from "lucide-react";
import { useEffect, useState } from "react";
import DialogBase from "../dialogBase";

interface EditTodoListI {
  yes: (newTitle: string) => void;
  open: boolean;
  close: () => void;
  title: string;
}

const EditTodoList = ({ open, close, title, yes }: EditTodoListI) => {
  const [newTitle, setNewTitle] = useState(title);
  const onClose = () => {
    setNewTitle(title);
    close();
  };

  useEffect(() => {
    setNewTitle(title);
  }, [title]);

  return (
    <DialogBase show={open} close={onClose}>
      <div className="flex flex-col dark:bg-black bg-white border-primary border-2 rounded-lg p-6 w-[500px]">
        <div className="flex flex-row justify-between">
          <h1 className="text-4xl dark:text-white text-black font-bold">
            Todo List
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
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() => yes(newTitle.trim())}
        >
          Save Todo List
        </button>
      </div>
    </DialogBase>
  );
};

export default EditTodoList;
