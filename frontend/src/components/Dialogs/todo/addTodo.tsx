import { X } from "lucide-react";
import { Dispatch, SetStateAction, useContext, useState } from "react";
import { TodoContext } from "../../../provider/todoProvider";
import DialogBase from "../dialogBase";

interface AddTodoI {
  open: boolean;
  close: () => void;
}

const AddTodo = ({ open, close }: AddTodoI) => {
  const { addTodo } = useContext(TodoContext)!;
  const [title, setTitle] = useState("New Item");
  const [deadline, setDeadline] = useState(null) as [
    string | null,
    Dispatch<SetStateAction<string | null>>
  ];

  const onClose = () => {
    setTitle("New Item");
    setDeadline("reset");
    //Necessary to clean date input if not filled out fully
    setTimeout(() => {
      setDeadline(null);
    }, 100);
    close();
  };

  return (
    <DialogBase show={open} close={onClose}>
      <div className="flex flex-col dark:bg-black bg-white border-primary border-2 rounded-lg p-6 w-[500px]">
        <div className="flex flex-row justify-between">
          <h1 className="text-4xl dark:text-white text-black font-bold">
            Add Todo
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
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <span className="dark:text-white text-black mt-6">Date (optional)</span>
        <input
          type="datetime-local"
          className="dark:text-white text-black border-2 dark:border-white border-black p-4 rounded-lg mt-1"
          value={deadline ?? ""}
          onChange={(e) => {
            setDeadline(0 === e.target.value.length ? null : e.target.value);
          }}
        />
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() => {
            addTodo(title.trim(), deadline);
            onClose();
          }}
        >
          Save Todo
        </button>
      </div>
    </DialogBase>
  );
};

export default AddTodo;
