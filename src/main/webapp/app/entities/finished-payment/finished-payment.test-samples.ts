import { IFinishedPayment, NewFinishedPayment } from './finished-payment.model';

export const sampleWithRequiredData: IFinishedPayment = {
  id: 11647,
};

export const sampleWithPartialData: IFinishedPayment = {
  id: 39048,
  price: 53622,
  approve: false,
};

export const sampleWithFullData: IFinishedPayment = {
  id: 52837,
  price: 5613,
  name: 'woot orange',
  approve: false,
};

export const sampleWithNewData: NewFinishedPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
