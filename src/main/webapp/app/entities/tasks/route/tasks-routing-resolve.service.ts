import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITasks } from '../tasks.model';
import { TasksService } from '../service/tasks.service';

export const tasksResolve = (route: ActivatedRouteSnapshot): Observable<null | ITasks> => {
  const id = route.params['id'];
  if (id) {
    return inject(TasksService)
      .find(id)
      .pipe(
        mergeMap((tasks: HttpResponse<ITasks>) => {
          if (tasks.body) {
            return of(tasks.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default tasksResolve;
