import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinishedPayment, NewFinishedPayment } from '../finished-payment.model';

export type PartialUpdateFinishedPayment = Partial<IFinishedPayment> & Pick<IFinishedPayment, 'id'>;

export type EntityResponseType = HttpResponse<IFinishedPayment>;
export type EntityArrayResponseType = HttpResponse<IFinishedPayment[]>;

@Injectable({ providedIn: 'root' })
export class FinishedPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/finished-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(finishedPayment: NewFinishedPayment): Observable<EntityResponseType> {
    return this.http.post<IFinishedPayment>(this.resourceUrl, finishedPayment, { observe: 'response' });
  }

  update(finishedPayment: IFinishedPayment): Observable<EntityResponseType> {
    return this.http.put<IFinishedPayment>(`${this.resourceUrl}/${this.getFinishedPaymentIdentifier(finishedPayment)}`, finishedPayment, {
      observe: 'response',
    });
  }

  partialUpdate(finishedPayment: PartialUpdateFinishedPayment): Observable<EntityResponseType> {
    return this.http.patch<IFinishedPayment>(`${this.resourceUrl}/${this.getFinishedPaymentIdentifier(finishedPayment)}`, finishedPayment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFinishedPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinishedPayment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinishedPaymentIdentifier(finishedPayment: Pick<IFinishedPayment, 'id'>): number {
    return finishedPayment.id;
  }

  compareFinishedPayment(o1: Pick<IFinishedPayment, 'id'> | null, o2: Pick<IFinishedPayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinishedPaymentIdentifier(o1) === this.getFinishedPaymentIdentifier(o2) : o1 === o2;
  }

  addFinishedPaymentToCollectionIfMissing<Type extends Pick<IFinishedPayment, 'id'>>(
    finishedPaymentCollection: Type[],
    ...finishedPaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const finishedPayments: Type[] = finishedPaymentsToCheck.filter(isPresent);
    if (finishedPayments.length > 0) {
      const finishedPaymentCollectionIdentifiers = finishedPaymentCollection.map(
        finishedPaymentItem => this.getFinishedPaymentIdentifier(finishedPaymentItem)!
      );
      const finishedPaymentsToAdd = finishedPayments.filter(finishedPaymentItem => {
        const finishedPaymentIdentifier = this.getFinishedPaymentIdentifier(finishedPaymentItem);
        if (finishedPaymentCollectionIdentifiers.includes(finishedPaymentIdentifier)) {
          return false;
        }
        finishedPaymentCollectionIdentifiers.push(finishedPaymentIdentifier);
        return true;
      });
      return [...finishedPaymentsToAdd, ...finishedPaymentCollection];
    }
    return finishedPaymentCollection;
  }
}
