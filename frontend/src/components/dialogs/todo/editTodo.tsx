import { X } from "lucide-react";
import { useEffect, useState } from "react";
import { joinDeadline, splitDeadline } from "../../../utils/deadline";
import DialogBase from "../dialogBase";

interface EditTodoI {
  yes: (newTitle: string, newDeadline: string | null) => void;
  open: boolean;
  close: () => void;
  title: string;
  deadline: string | null;
}

const inputClass =
  "dark:text-white text-black border-2 dark:border-white border-black p-4 rounded-lg mt-1";

const EditTodo = ({ open, close, title, deadline, yes }: EditTodoI) => {
  const [newTitle, setNewTitle] = useState(title);
  const [newDate, setNewDate] = useState(splitDeadline(deadline).date);
  const [newTime, setNewTime] = useState(splitDeadline(deadline).time);
  // changing the key of the date and time inputs also removes a partly typed date or time
  const [resetKey, setResetKey] = useState(0);

  const takeOver = () => {
    const { date, time } = splitDeadline(deadline);
    setNewTitle(title);
    setNewDate(date);
    setNewTime(time);
    setResetKey((key) => key + 1);
  };

  const onClose = () => {
    takeOver();
    close();
  };

  // the dialog is created only once per item, so take over the current values of the item whenever they change
  useEffect(takeOver, [title, deadline]);

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
          className={inputClass}
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
        />
        <div className="flex flex-row gap-4 mt-6">
          <div className="flex flex-col flex-1">
            <span className="dark:text-white text-black">Date (optional)</span>
            <input
              key={`date-${resetKey}`}
              type="date"
              className={inputClass}
              value={newDate}
              onChange={(e) => {
                setNewDate(e.target.value);
                if (!e.target.value) {
                  setNewTime("");
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
              value={newTime}
              disabled={!newDate}
              onChange={(e) => setNewTime(e.target.value)}
            />
          </div>
        </div>
        <button
          className="mt-12 text-white bg-primary rounded-lg py-4 cursor-pointer"
          onClick={() =>
            newTitle.trim() && yes(newTitle, joinDeadline(newDate, newTime))
          }
        >
          Save Todo
        </button>
      </div>
    </DialogBase>
  );
};

export default EditTodo;
