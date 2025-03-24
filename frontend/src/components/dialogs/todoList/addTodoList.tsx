import { X } from "lucide-react";
import { useContext, useState } from "react";
import { TodoListContext } from "../../../provider/todoListProvider";
import DialogBase from "../dialogBase";

interface AddTodoListI {
  open: boolean;
  close: () => void;
}

const AddTodoList = ({ open, close }: AddTodoListI) => {
  const { addTaskList } = useContext(TodoListContext)!;
  const [title, setTitle] = useState("New List");
  const onClose = () => {
    setTitle("New List");
    close();
  };

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
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() => {
            addTaskList(title);
            onClose();
          }}
        >
          Create Todo List
        </button>
      </div>
    </DialogBase>
  );
};

export default AddTodoList;
