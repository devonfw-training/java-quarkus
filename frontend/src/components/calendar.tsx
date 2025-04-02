import moment from "moment";
import { useContext } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import { TodoContext } from "../provider/todoProvider";

const localizer = momentLocalizer(moment);

const CalendarView = () => {
  const { todos } = useContext(TodoContext)!;

  return (
    <div className="dark:bg-light-black w-full p-12 flex flex-1 basis-0 overflow-y-auto flex-col">
      <Calendar
        localizer={localizer}
        defaultDate={new Date()}
        defaultView="month"
        events={todos
          .filter((e) => null != e.deadline)
          .map((e) => {
            return {
              title: e.title,
              start: new Date(e.deadline),
              end: new Date(e.deadline),
            };
          })}
        style={{ height: "100%" }}
      />
    </div>
  );
};

export default CalendarView;
