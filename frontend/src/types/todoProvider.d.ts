interface TodoInterfaceI {
  todos: TaskItemType[];

  setTodos: React.Dispatch<React.SetStateAction<TaskItemType[]>>;
  markComplete: (id: number) => void;
  delTodo: (id: number) => void;
  deleteAll: () => void;
  editTodo: (id: number, text: string, deadline: string | null) => void;
  addTodo: (title: string, deadline: string | null) => void;
  addRandomTodo: () => void;
  moveTodo: (old: number, new_: number) => void;
  markStar: (id: number) => void;
  applyFilter: (
    todos: TaskItemTypeI[],
    filter: SelectedFilterE
  ) => TaskItemTypeI[];
  applySort: (
    todos: TaskItemTypeI[],
    sort: {
      selectedSort: SelectedSortE;
      selectedSortOrder: SelectedSortOrderE;
    }
  ) => TaskItemTypeI[];
}
