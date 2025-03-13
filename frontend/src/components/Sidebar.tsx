import { PlusIcon } from "lucide-react";
import { useContext } from "react";
import { Link, useRoute } from "wouter";
import { MainContext } from "../context/MainContext";

export default function Sidebar() {
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;
  const { taskLists } = useContext(MainContext)!;

  return (
    <div className="h-full bg-light-gray w-3xs flex flex-col">
      <p className="pl-6 pt-9 pb-6 font-bold text-lg text-black">Your lists</p>
      <hr className="border-light-gray2" />
      <div className="flex items-center gap-2.5 py-4 pl-5 cursor-pointer">
        <PlusIcon className="text-black" />
        <p className="text-black">Add list</p>
      </div>
      <div className="overflow-y-scroll flex-1 basis-0">
        {taskLists.map((e, i) => {
          return (
            <Link href={`/${e.id}`} key={i}>
              <div
                className={`cursor-pointer ${
                  listId && e.id === +listId ? "bg-primary" : ""
                }`}
              >
                <p
                  className={`py-4 pl-6 ${
                    listId && e.id === +listId ? "text-white" : "text-black"
                  }`}
                >
                  {e.title}
                </p>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
