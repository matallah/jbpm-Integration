import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITasks, NewTasks } from '../tasks.model';

export type PartialUpdateTasks = Partial<ITasks> & Pick<ITasks, 'id'>;

export type EntityResponseType = HttpResponse<ITasks>;
export type EntityArrayResponseType = HttpResponse<ITasks[]>;

@Injectable({ providedIn: 'root' })
export class TasksService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tasks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tasks: NewTasks): Observable<EntityResponseType> {
    return this.http.post<ITasks>(this.resourceUrl, tasks, { observe: 'response' });
  }

  update(tasks: ITasks): Observable<EntityResponseType> {
    return this.http.put<ITasks>(`${this.resourceUrl}/${this.getTasksIdentifier(tasks)}`, tasks, { observe: 'response' });
  }

  partialUpdate(tasks: PartialUpdateTasks): Observable<EntityResponseType> {
    return this.http.patch<ITasks>(`${this.resourceUrl}/${this.getTasksIdentifier(tasks)}`, tasks, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITasks>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITasks[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTasksIdentifier(tasks: Pick<ITasks, 'id'>): number {
    return tasks.id;
  }

  compareTasks(o1: Pick<ITasks, 'id'> | null, o2: Pick<ITasks, 'id'> | null): boolean {
    return o1 && o2 ? this.getTasksIdentifier(o1) === this.getTasksIdentifier(o2) : o1 === o2;
  }

  addTasksToCollectionIfMissing<Type extends Pick<ITasks, 'id'>>(
    tasksCollection: Type[],
    ...tasksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tasks: Type[] = tasksToCheck.filter(isPresent);
    if (tasks.length > 0) {
      const tasksCollectionIdentifiers = tasksCollection.map(tasksItem => this.getTasksIdentifier(tasksItem)!);
      const tasksToAdd = tasks.filter(tasksItem => {
        const tasksIdentifier = this.getTasksIdentifier(tasksItem);
        if (tasksCollectionIdentifiers.includes(tasksIdentifier)) {
          return false;
        }
        tasksCollectionIdentifiers.push(tasksIdentifier);
        return true;
      });
      return [...tasksToAdd, ...tasksCollection];
    }
    return tasksCollection;
  }
}
