import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OngoingPaymentComponent } from './list/ongoing-payment.component';
import { OngoingPaymentDetailComponent } from './detail/ongoing-payment-detail.component';
import { OngoingPaymentUpdateComponent } from './update/ongoing-payment-update.component';
import OngoingPaymentResolve from './route/ongoing-payment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ongoingPaymentRoute: Routes = [
  {
    path: '',
    component: OngoingPaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OngoingPaymentDetailComponent,
    resolve: {
      ongoingPayment: OngoingPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OngoingPaymentUpdateComponent,
    resolve: {
      ongoingPayment: OngoingPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OngoingPaymentUpdateComponent,
    resolve: {
      ongoingPayment: OngoingPaymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ongoingPaymentRoute;
