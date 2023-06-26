import { ITasks, NewTasks } from './tasks.model';

export const sampleWithRequiredData: ITasks = {
  id: 80259,
};

export const sampleWithPartialData: ITasks = {
  id: 41198,
  taskName: 'female Mouse Savings',
  taskStatus: 'Rustic',
};

export const sampleWithFullData: ITasks = {
  id: 9609,
  taskId: 40670,
  taskName: 'Southwest Cargo Madera',
  taskStatus: 'incentivize Account',
  price: 89130,
  approve: false,
};

export const sampleWithNewData: NewTasks = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
