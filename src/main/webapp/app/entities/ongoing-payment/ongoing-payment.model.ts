export interface IOngoingPayment {
  id: number;
  price?: number | null;
  name?: string | null;
}

export type NewOngoingPayment = Omit<IOngoingPayment, 'id'> & { id: null };
