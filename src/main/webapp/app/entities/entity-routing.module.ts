import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'process-instances',
        data: { pageTitle: 'jBpmApp.processInstances.home.title' },
        loadChildren: () => import('./process-instances/process-instances.routes'),
      },
      {
        path: 'tasks',
        data: { pageTitle: 'jBpmApp.tasks.home.title' },
        loadChildren: () => import('./tasks/tasks.routes'),
      },
      {
        path: 'finished-payment',
        data: { pageTitle: 'jBpmApp.finishedPayment.home.title' },
        loadChildren: () => import('./finished-payment/finished-payment.routes'),
      },
      {
        path: 'ongoing-payment',
        data: { pageTitle: 'jBpmApp.ongoingPayment.home.title' },
        loadChildren: () => import('./ongoing-payment/ongoing-payment.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
