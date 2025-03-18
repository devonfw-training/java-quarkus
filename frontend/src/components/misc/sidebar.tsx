import { CalendarDays, PlusIcon, Trash } from "lucide-react";
import { RefObject, useContext, useRef } from "react";
import { Link, useRoute } from "wouter";
import useShowOnHover from "../../hooks/showOnHover";
import { MainContext } from "../../provider/mainProvider";
import { TodoContext } from "../../provider/todoProvider";
import { TaskListTypeI } from "../../types/types";

export default function Sidebar() {
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const { changeShowCalendar } = useContext(MainContext)!;
  const { taskLists } = useContext(TodoContext)!;

  const map = taskLists.map((e, i) => {
    return <ListItem key={i} e={e} i={i} listId={listId} />;
  });

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
      <div className="overflow-y-auto flex-1 basis-0">{map}</div>
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

interface ListItemI {
  e: TaskListTypeI;
  i: number;
  listId?: string;
}

const ListItem = (props: ListItemI) => {
  const listItemRef: RefObject<HTMLDivElement> = useRef(null);
  const deleteIconRef: RefObject<HTMLDivElement> = useRef(null);
  useShowOnHover(listItemRef, deleteIconRef);

  return (
    <div ref={listItemRef}>
      <Link href={`/${props.e.id}`} key={props.i}>
        <div
          className={`cursor-pointer flex justify-between gap-2.5 items-center ${
            props.listId && props.e.id === +props.listId
              ? "bg-primary"
              : "dark:hover:bg-primary hover:bg-light-primary"
          }`}
        >
          <p
            className={`my-4 ml-6 ${
              props.listId && props.e.id === +props.listId
                ? "text-white"
                : "dark:text-white text-black"
            }`}
          >
            {props.e.title}
          </p>
          <div
            ref={deleteIconRef}
            className="mr-2 hover:bg-white dark:hover:bg-black rounded-full w-10 h-10 justify-center items-center hidden"
          >
            <Trash className="w-6 h-6 text-red" />
          </div>
        </div>
      </Link>
    </div>
  );
};
