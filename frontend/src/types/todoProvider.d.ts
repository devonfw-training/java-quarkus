interface TodoInterfaceI {
  todos: TaskItemType[];
  taskLists: TaskListType[];

  setTodos: React.Dispatch<React.SetStateAction<TaskItemType[]>>;
  setTaskLists: React.Dispatch<React.SetStateAction<TaskListType[]>>;
  markComplete: (id: number) => void;
  delTodo: (id: number) => void;
  deleteAll: () => void;
  editTodo: (id: number, text: string, deadline?: string) => void;
  addTodo: (title: string) => void;
  moveTodo: (old: number, new_: number) => void;
  markStar: (id: number) => void;
}
