import { CalendarDays, PlusIcon } from "lucide-react";
import { useContext } from "react";
import { Link, useRoute } from "wouter";
import { MainContext } from "../../provider/mainProvider";
import { TodoContext } from "../../provider/todoProvider";

export default function Sidebar() {
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const { changeShowCalendar } = useContext(MainContext)!;
  const { taskLists } = useContext(TodoContext)!;

  return (
    <div className="h-full dark:bg-black bg-light-gray w-3xs flex flex-col">
      <p className="pl-6 pt-9 pb-6 font-bold text-lg dark:text-white text-black">
        Your lists
      </p>
      <hr className="dark:border-light-black border-light-gray2" />
      <div className="flex items-center gap-2.5 py-4 pl-5 cursor-pointer dark:hover:bg-primary hover:bg-light-primary">
        <PlusIcon className="dark:text-white text-black" />
        <p className="dark:text-white text-black">Add list</p>
      </div>
      <div className="overflow-y-auto flex-1 basis-0">
        {taskLists.map((e, i) => {
          return (
            <Link href={`/${e.id}`} key={i}>
              <div
                className={`cursor-pointer ${
                  listId && e.id === +listId
                    ? "bg-primary"
                    : "dark:hover:bg-primary hover:bg-light-primary"
                }`}
              >
                <p
                  className={`py-4 pl-6 ${
                    listId && e.id === +listId
                      ? "text-white"
                      : "dark:text-white text-black"
                  }`}
                >
                  {e.title}
                </p>
              </div>
            </Link>
          );
        })}
      </div>
      <div
        className="flex flex-row gap-2.5 justify-center items-center dark:bg-primary bg-light-primary rounded-t-3xl py-8 cursor-pointer"
        onClick={changeShowCalendar}
      >
        <CalendarDays className="dark:text-white text-black" />
        <p className="dark:text-white text-black text-md">Switch to calendar</p>
      </div>
    </div>
  );
}
