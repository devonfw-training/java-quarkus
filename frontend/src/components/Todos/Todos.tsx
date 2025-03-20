import { Edit2, Filter, ListFilter, Plus } from "lucide-react";
import { useContext, useEffect, useRef, useState } from "react";
import FlipMove from "react-flip-move";
import { useRoute } from "wouter";
import useHasOverflow from "../../hooks/hasOverflow";
import useScrollbarWidth from "../../hooks/scrollBarWidth";
import { TodoListContext } from "../../provider/todoListProvider";
import { TodoContext } from "../../provider/todoProvider";
import { TaskItemTypeI } from "../../types/types";
import AddTodo from "../dialogs/todo/addTodo";
import EditTodoList from "../dialogs/todoList/editTodoList";
import { FilterMenu, SelectedFilterE } from "../menus/filterMenu";
import { SelectedSortE, SelectedSortOrderE, SortMenu } from "../menus/sortMenu";
import Todo from "./todo";

const Todos = () => {
  const { taskLists, editTodoList } = useContext(TodoListContext)!;
  const { todos } = useContext(TodoContext)!;
  const [, params] = useRoute("/:listId");
  const listId = params?.listId;

  const [showAddTodoOpen, setShowAddTodoOpen] = useState(false);
  const [showEditTodoList, setShowEditTodoList] = useState(false);

  const taskListTitle =
    taskLists.filter((e) => listId && e.id === +listId)[0]?.title ??
    (undefined === listId ? "Please select a list" : "Loading...");

  return (
    <div className="dark:bg-light-black w-full p-12 flex flex-col">
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row gap-2.5 items-baseline">
          <h1 className="text-3xl dark:text-white">{taskListTitle}</h1>
          <Edit2
            className="cursor-pointer dark:text-white"
            onClick={() => setShowEditTodoList(true)}
          />
        </div>
        <Plus
          className="cursor-pointer dark:text-light-primary text-primary w-10 h-10"
          onClick={() => setShowAddTodoOpen(true)}
        />
      </div>
      <div className="mt-6 flex flex-row gap-6 w-full grow">
        <List title="Tasks" todos={todos.filter((e) => !e.completed)} />
        <List title="Done" todos={todos.filter((e) => e.completed)} />
      </div>

      <AddTodo open={showAddTodoOpen} close={() => setShowAddTodoOpen(false)} />
      <EditTodoList
        open={showEditTodoList}
        close={() => setShowEditTodoList(false)}
        yes={(newTitle: string) => {
          setShowEditTodoList(false);
          editTodoList(newTitle);
        }}
        title={taskListTitle}
      ></EditTodoList>
    </div>
  );
};

interface ListI {
  title: string;
  todos: TaskItemTypeI[];
}

function List({ title, todos }: ListI) {
  const { applyFilter, applySort } = useContext(TodoContext)!;
  const scrollContainerRef = useRef(null);
  const scrollbarWidth = useScrollbarWidth();
  const hasOverflow = useHasOverflow(scrollContainerRef);

  const [, setDeleteSnackOpen] = useState(false);
  const [, setEditSnackOpen] = useState(false);

  /**
   *
   * APPLY FILTER
   *
   */

  const filterRef = useRef(null);
  const [showFilter, setShowFilter] = useState(false);
  const [selectedFilter, setSelectedFilter] = useState(SelectedFilterE.NONE);

  const [filteredTodos, setFilteredTodos] = useState([] as TaskItemTypeI[]);

  useEffect(() => {
    setFilteredTodos(applyFilter(todos, selectedFilter));
  }, [applyFilter, selectedFilter, todos]);

  /**
   *
   * APPLY SORT ON FILTERED ITEMS
   *
   */

  const sortRef = useRef(null);
  const [showSort, setShowSort] = useState(false);
  const [selectedSort, setSelectedSort] = useState({
    selectedSort: SelectedSortE.NONE,
    selectedSortOrder: SelectedSortOrderE.NOT_SELECTED,
  });

  const [sortedTodos, setSortedTodos] = useState([] as TaskItemTypeI[]);

  useEffect(() => {
    setSortedTodos(applySort(filteredTodos, selectedSort));
  }, [applySort, selectedSort, filteredTodos]);

  return (
    <div className="flex flex-col w-full">
      <div className="flex flex-row justify-between items-center relative">
        <p className="dark:text-white text-black">{`${title} - ${sortedTodos.length}`}</p>
        <div
          className={`flex flex-row gap-2.5 transition-all `}
          style={{
            marginRight: `${
              hasOverflow
                ? `calc(calc(var(--spacing) * 2.5) + ${scrollbarWidth}px)`
                : ""
            }`,
          }}
        >
          <Filter
            ref={filterRef}
            className={`cursor-pointer ${
              selectedFilter !== SelectedFilterE.NONE
                ? "text-primary"
                : "text-light-primary"
            }`}
            onClick={() => setShowFilter(!showFilter)}
          />
          <ListFilter
            ref={sortRef}
            className={`cursor-pointer ${
              selectedSort.selectedSort !== SelectedSortE.NONE
                ? "text-primary"
                : "text-light-primary"
            }`}
            onClick={() => setShowSort(!showSort)}
          />
        </div>
        <FilterMenu
          ignoreClick={[filterRef]}
          showFilter={showFilter}
          setShowFilter={setShowFilter}
          setSelectedFilter={setSelectedFilter}
          selectedFilter={selectedFilter}
        />
        <SortMenu
          ignoreClick={[sortRef]}
          showSort={showSort}
          setShowSort={setShowSort}
          setSelectedSort={setSelectedSort}
          selectedSort={selectedSort}
        />
      </div>
      <div
        ref={scrollContainerRef}
        className={`mt-2.5 overflow-y-auto flex-1 basis-0 ${
          hasOverflow ? "pr-2.5" : ""
        }`}
      >
        <div>
          <FlipMove className="gap-2.5 flex flex-col">
            {sortedTodos.map((todo, i) => {
              return (
                <Todo
                  todo={todo}
                  key={todo.id}
                  onDelete={() => setDeleteSnackOpen(true)}
                  onEdit={() => setEditSnackOpen(true)}
                />
              );
            })}
          </FlipMove>
        </div>
      </div>
    </div>
  );
}

export default Todos;
