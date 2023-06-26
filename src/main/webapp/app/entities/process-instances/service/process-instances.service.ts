import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProcessInstances, NewProcessInstances } from '../process-instances.model';

export type PartialUpdateProcessInstances = Partial<IProcessInstances> & Pick<IProcessInstances, 'id'>;

export type EntityResponseType = HttpResponse<IProcessInstances>;
export type EntityArrayResponseType = HttpResponse<IProcessInstances[]>;

@Injectable({ providedIn: 'root' })
export class ProcessInstancesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/process-instances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(processInstances: NewProcessInstances): Observable<EntityResponseType> {
    return this.http.post<IProcessInstances>(this.resourceUrl, processInstances, { observe: 'response' });
  }

  update(processInstances: IProcessInstances): Observable<EntityResponseType> {
    return this.http.put<IProcessInstances>(
      `${this.resourceUrl}/${this.getProcessInstancesIdentifier(processInstances)}`,
      processInstances,
      { observe: 'response' }
    );
  }

  partialUpdate(processInstances: PartialUpdateProcessInstances): Observable<EntityResponseType> {
    return this.http.patch<IProcessInstances>(
      `${this.resourceUrl}/${this.getProcessInstancesIdentifier(processInstances)}`,
      processInstances,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProcessInstances>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProcessInstances[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  query2(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProcessInstances[]>(this.resourceUrl + '-new', { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProcessInstancesIdentifier(processInstances: Pick<IProcessInstances, 'id'>): number {
    return processInstances.id;
  }

  compareProcessInstances(o1: Pick<IProcessInstances, 'id'> | null, o2: Pick<IProcessInstances, 'id'> | null): boolean {
    return o1 && o2 ? this.getProcessInstancesIdentifier(o1) === this.getProcessInstancesIdentifier(o2) : o1 === o2;
  }

  addProcessInstancesToCollectionIfMissing<Type extends Pick<IProcessInstances, 'id'>>(
    processInstancesCollection: Type[],
    ...processInstancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const processInstances: Type[] = processInstancesToCheck.filter(isPresent);
    if (processInstances.length > 0) {
      const processInstancesCollectionIdentifiers = processInstancesCollection.map(
        processInstancesItem => this.getProcessInstancesIdentifier(processInstancesItem)!
      );
      const processInstancesToAdd = processInstances.filter(processInstancesItem => {
        const processInstancesIdentifier = this.getProcessInstancesIdentifier(processInstancesItem);
        if (processInstancesCollectionIdentifiers.includes(processInstancesIdentifier)) {
          return false;
        }
        processInstancesCollectionIdentifiers.push(processInstancesIdentifier);
        return true;
      });
      return [...processInstancesToAdd, ...processInstancesCollection];
    }
    return processInstancesCollection;
  }
}
