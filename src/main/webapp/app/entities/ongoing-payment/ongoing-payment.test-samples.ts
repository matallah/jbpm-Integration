import { IOngoingPayment, NewOngoingPayment } from './ongoing-payment.model';

export const sampleWithRequiredData: IOngoingPayment = {
  id: 66965,
};

export const sampleWithPartialData: IOngoingPayment = {
  id: 65589,
  price: 43736,
  name: 'Wooden East',
};

export const sampleWithFullData: IOngoingPayment = {
  id: 80542,
  price: 65969,
  name: 'ride Gasoline',
};

export const sampleWithNewData: NewOngoingPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
