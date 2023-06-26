export interface IProcessInstances {
  id: number;
  processInstanceId?: number | null;
  processId?: string | null;
  processName?: string | null;
}

export type NewProcessInstances = Omit<IProcessInstances, 'id'> & { id: null };
