import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProcessInstancesComponent } from './list/process-instances.component';
import { ProcessInstancesDetailComponent } from './detail/process-instances-detail.component';
import { ProcessInstancesUpdateComponent } from './update/process-instances-update.component';
import ProcessInstancesResolve from './route/process-instances-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const processInstancesRoute: Routes = [
  {
    path: '',
    component: ProcessInstancesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcessInstancesDetailComponent,
    resolve: {
      processInstances: ProcessInstancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcessInstancesUpdateComponent,
    resolve: {
      processInstances: ProcessInstancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcessInstancesUpdateComponent,
    resolve: {
      processInstances: ProcessInstancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default processInstancesRoute;
