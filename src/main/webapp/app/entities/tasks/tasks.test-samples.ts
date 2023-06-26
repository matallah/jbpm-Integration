import { ITasks, NewTasks } from './tasks.model';

export const sampleWithRequiredData: ITasks = {
  id: 93284,
};

export const sampleWithPartialData: ITasks = {
  id: 53087,
  taskId: 86020,
  taskName: 'groupware',
};

export const sampleWithFullData: ITasks = {
  id: 75779,
  taskId: 71221,
  taskName: 'Mouse',
  taskStatus: 'Citlalli West',
};

export const sampleWithNewData: NewTasks = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
