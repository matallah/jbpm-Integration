import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FinishedPaymentComponent } from './list/finished-payment.component';
import { FinishedPaymentDetailComponent } from './detail/finished-payment-detail.component';
import { FinishedPaymentUpdateComponent } from './update/finished-payment-update.component';
import FinishedPaymentResolve from './route/finished-payment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const finishedPaymentRoute: Routes = [
  {
    path: '',
    component: FinishedPaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinishedPaymentDetailComponent,
    resolve: {
      finishedPayment: FinishedPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinishedPaymentUpdateComponent,
    resolve: {
      finishedPayment: FinishedPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinishedPaymentUpdateComponent,
    resolve: {
      finishedPayment: FinishedPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default finishedPaymentRoute;
