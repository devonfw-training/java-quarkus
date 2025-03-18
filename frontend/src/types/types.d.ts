export interface TaskItemTypeI {
  id: number;
  title: string;
  completed: any;
  starred: any;
  taskListId: number;
  deadline?: string;
}

export interface TaskListTypeI {
  id: number;
  title: string;
}
