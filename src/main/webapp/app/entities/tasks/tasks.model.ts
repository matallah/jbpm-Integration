export interface ITasks {
  id: number;
  taskId?: number | null;
  taskName?: string | null;
  taskStatus?: string | null;
  price?: number | null;
  approve?: boolean | null;
}

export type NewTasks = Omit<ITasks, 'id'> & { id: null };
