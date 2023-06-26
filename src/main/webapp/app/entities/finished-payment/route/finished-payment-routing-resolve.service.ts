import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinishedPayment } from '../finished-payment.model';
import { FinishedPaymentService } from '../service/finished-payment.service';

export const finishedPaymentResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinishedPayment> => {
  const id = route.params['id'];
  if (id) {
    return inject(FinishedPaymentService)
      .find(id)
      .pipe(
        mergeMap((finishedPayment: HttpResponse<IFinishedPayment>) => {
          if (finishedPayment.body) {
            return of(finishedPayment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default finishedPaymentResolve;
