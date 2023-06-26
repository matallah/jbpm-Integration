import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOngoingPayment } from '../ongoing-payment.model';
import { OngoingPaymentService } from '../service/ongoing-payment.service';

export const ongoingPaymentResolve = (route: ActivatedRouteSnapshot): Observable<null | IOngoingPayment> => {
  const id = route.params['id'];
  if (id) {
    return inject(OngoingPaymentService)
      .find(id)
      .pipe(
        mergeMap((ongoingPayment: HttpResponse<IOngoingPayment>) => {
          if (ongoingPayment.body) {
            return of(ongoingPayment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ongoingPaymentResolve;
