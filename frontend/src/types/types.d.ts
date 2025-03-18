export interface TaskItemType {
  id: number;
  title: string;
  completed: any;
  starred: any;
  taskListId: number;
  deadline?: string;
}

export interface TaskListType {
  id: number;
  title: string;
}
