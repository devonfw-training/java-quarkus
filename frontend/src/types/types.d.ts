export interface TaskItemTypeI {
  id: number;
  title: string;
  version: number;
  completed: any;
  starred: any;
  taskListId: number;
  deadline: string | null;
}

export interface TaskListTypeI {
  id: number;
  version: number;
  title: string;
}
