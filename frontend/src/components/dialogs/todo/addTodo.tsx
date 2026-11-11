import { X } from "lucide-react";
import { useContext, useState } from "react";
import { TodoContext } from "../../../provider/todoProvider";
import { joinDeadline } from "../../../utils/deadline";
import DialogBase from "../dialogBase";

interface AddTodoI {
  open: boolean;
  close: () => void;
}

const inputClass =
  "dark:text-white text-black border-2 dark:border-white border-black p-4 rounded-lg mt-1";

const AddTodo = ({ open, close }: AddTodoI) => {
  const { addTodo } = useContext(TodoContext)!;
  const [title, setTitle] = useState("New Item");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  // changing the key of the date and time inputs also removes a partly typed date or time
  const [resetKey, setResetKey] = useState(0);

  const onClose = () => {
    setTitle("New Item");
    setDate("");
    setTime("");
    setResetKey((key) => key + 1);
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
          className={inputClass}
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <div className="flex flex-row gap-4 mt-6">
          <div className="flex flex-col flex-1">
            <span className="dark:text-white text-black">Date (optional)</span>
            <input
              key={`date-${resetKey}`}
              type="date"
              className={inputClass}
              value={date}
              onChange={(e) => {
                setDate(e.target.value);
                if (!e.target.value) {
                  setTime("");
                }
              }}
            />
          </div>
          <div className="flex flex-col flex-1">
            <span className="dark:text-white text-black">Time (optional)</span>
            <input
              key={`time-${resetKey}`}
              type="time"
              className={inputClass}
              value={time}
              disabled={!date}
              onChange={(e) => setTime(e.target.value)}
            />
          </div>
        </div>
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() => {
            addTodo(title.trim(), joinDeadline(date, time));
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
