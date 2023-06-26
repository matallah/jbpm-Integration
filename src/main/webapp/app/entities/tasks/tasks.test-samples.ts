import { ITasks, NewTasks } from './tasks.model';

export const sampleWithRequiredData: ITasks = {
  id: 44797,
};

export const sampleWithPartialData: ITasks = {
  id: 71221,
  taskId: 11276,
  taskName: 'Tools',
  name: 'Bedfordshire',
};

export const sampleWithFullData: ITasks = {
  id: 22679,
  taskId: 13800,
  taskName: 'Credit',
  taskStatus: 'Cargo Madera Hong',
  price: 41896,
  approve: false,
  name: 'easily Organized Cambridgeshire',
};

export const sampleWithNewData: NewTasks = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
