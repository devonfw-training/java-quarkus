interface TodoListInterfaceI {
  taskLists: TaskListType[];

  setTaskLists: React.Dispatch<React.SetStateAction<TaskListType[]>>;
}
