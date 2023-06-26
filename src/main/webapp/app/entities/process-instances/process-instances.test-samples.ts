import { IProcessInstances, NewProcessInstances } from './process-instances.model';

export const sampleWithRequiredData: IProcessInstances = {
  id: 30697,
};

export const sampleWithPartialData: IProcessInstances = {
  id: 25919,
  processInstanceId: 61736,
};

export const sampleWithFullData: IProcessInstances = {
  id: 36301,
  processInstanceId: 3227,
  processId: 'Chief Divide',
  processName: 'Facilitator boohoo',
};

export const sampleWithNewData: NewProcessInstances = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
