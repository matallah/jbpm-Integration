import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProcessInstancesFormService, ProcessInstancesFormGroup } from './process-instances-form.service';
import { IProcessInstances } from '../process-instances.model';
import { ProcessInstancesService } from '../service/process-instances.service';

@Component({
  standalone: true,
  selector: 'jhi-process-instances-update',
  templateUrl: './process-instances-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcessInstancesUpdateComponent implements OnInit {
  isSaving = false;
  processInstances: IProcessInstances | null = null;

  editForm: ProcessInstancesFormGroup = this.processInstancesFormService.createProcessInstancesFormGroup();

  constructor(
    protected processInstancesService: ProcessInstancesService,
    protected processInstancesFormService: ProcessInstancesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processInstances }) => {
      this.processInstances = processInstances;
      if (processInstances) {
        this.updateForm(processInstances);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const processInstances = this.processInstancesFormService.getProcessInstances(this.editForm);
    if (processInstances.id !== null) {
      this.subscribeToSaveResponse(this.processInstancesService.update(processInstances));
    } else {
      this.subscribeToSaveResponse(this.processInstancesService.create(processInstances));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcessInstances>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(processInstances: IProcessInstances): void {
    this.processInstances = processInstances;
    this.processInstancesFormService.resetForm(this.editForm, processInstances);
  }
}
