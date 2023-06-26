import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TasksComponent } from './list/tasks.component';
import { TasksDetailComponent } from './detail/tasks-detail.component';
import { TasksUpdateComponent } from './update/tasks-update.component';
import TasksResolve from './route/tasks-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tasksRoute: Routes = [
  {
    path: '',
    component: TasksComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TasksDetailComponent,
    resolve: {
      tasks: TasksResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TasksUpdateComponent,
    resolve: {
      tasks: TasksResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TasksUpdateComponent,
    resolve: {
      tasks: TasksResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tasksRoute;
