import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcessInstances } from '../process-instances.model';
import { ProcessInstancesService } from '../service/process-instances.service';

export const processInstancesResolve = (route: ActivatedRouteSnapshot): Observable<null | IProcessInstances> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProcessInstancesService)
      .find(id)
      .pipe(
        mergeMap((processInstances: HttpResponse<IProcessInstances>) => {
          if (processInstances.body) {
            return of(processInstances.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default processInstancesResolve;
