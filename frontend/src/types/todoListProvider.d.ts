interface TodoListInterfaceI {
  taskLists: TaskListType[];

  setTaskLists: React.Dispatch<React.SetStateAction<TaskListType[]>>;
  editTodoList: (newTitle: string) => void;
  addTaskList: (title: string) => void;
  delTaskList: (id: number) => void;
}
