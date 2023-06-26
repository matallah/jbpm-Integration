export interface IFinishedPayment {
  id: number;
  price?: number | null;
  name?: string | null;
  approve?: boolean | null;
}

export type NewFinishedPayment = Omit<IFinishedPayment, 'id'> & { id: null };
