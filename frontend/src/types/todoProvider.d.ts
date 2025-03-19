interface TodoInterfaceI {
  todos: TaskItemType[];

  setTodos: React.Dispatch<React.SetStateAction<TaskItemType[]>>;
  markComplete: (id: number) => void;
  delTodo: (id: number) => void;
  deleteAll: () => void;
  editTodo: (id: number, text: string, deadline: string | null) => void;
  addTodo: (title: string, deadline: string | null) => void;
  moveTodo: (old: number, new_: number) => void;
  markStar: (id: number) => void;
}
