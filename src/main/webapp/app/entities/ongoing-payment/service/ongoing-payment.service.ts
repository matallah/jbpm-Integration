import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOngoingPayment, NewOngoingPayment } from '../ongoing-payment.model';

export type PartialUpdateOngoingPayment = Partial<IOngoingPayment> & Pick<IOngoingPayment, 'id'>;

export type EntityResponseType = HttpResponse<IOngoingPayment>;
export type EntityArrayResponseType = HttpResponse<IOngoingPayment[]>;

@Injectable({ providedIn: 'root' })
export class OngoingPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ongoing-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ongoingPayment: NewOngoingPayment): Observable<EntityResponseType> {
    return this.http.post<IOngoingPayment>(this.resourceUrl, ongoingPayment, { observe: 'response' });
  }

  update(ongoingPayment: IOngoingPayment): Observable<EntityResponseType> {
    return this.http.put<IOngoingPayment>(`${this.resourceUrl}/${this.getOngoingPaymentIdentifier(ongoingPayment)}`, ongoingPayment, {
      observe: 'response',
    });
  }

  partialUpdate(ongoingPayment: PartialUpdateOngoingPayment): Observable<EntityResponseType> {
    return this.http.patch<IOngoingPayment>(`${this.resourceUrl}/${this.getOngoingPaymentIdentifier(ongoingPayment)}`, ongoingPayment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOngoingPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOngoingPayment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOngoingPaymentIdentifier(ongoingPayment: Pick<IOngoingPayment, 'id'>): number {
    return ongoingPayment.id;
  }

  compareOngoingPayment(o1: Pick<IOngoingPayment, 'id'> | null, o2: Pick<IOngoingPayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getOngoingPaymentIdentifier(o1) === this.getOngoingPaymentIdentifier(o2) : o1 === o2;
  }

  addOngoingPaymentToCollectionIfMissing<Type extends Pick<IOngoingPayment, 'id'>>(
    ongoingPaymentCollection: Type[],
    ...ongoingPaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ongoingPayments: Type[] = ongoingPaymentsToCheck.filter(isPresent);
    if (ongoingPayments.length > 0) {
      const ongoingPaymentCollectionIdentifiers = ongoingPaymentCollection.map(
        ongoingPaymentItem => this.getOngoingPaymentIdentifier(ongoingPaymentItem)!
      );
      const ongoingPaymentsToAdd = ongoingPayments.filter(ongoingPaymentItem => {
        const ongoingPaymentIdentifier = this.getOngoingPaymentIdentifier(ongoingPaymentItem);
        if (ongoingPaymentCollectionIdentifiers.includes(ongoingPaymentIdentifier)) {
          return false;
        }
        ongoingPaymentCollectionIdentifiers.push(ongoingPaymentIdentifier);
        return true;
      });
      return [...ongoingPaymentsToAdd, ...ongoingPaymentCollection];
    }
    return ongoingPaymentCollection;
  }
}
