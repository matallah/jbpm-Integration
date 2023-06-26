import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TasksFormService, TasksFormGroup } from './tasks-form.service';
import { ITasks } from '../tasks.model';
import { TasksService } from '../service/tasks.service';

@Component({
  standalone: true,
  selector: 'jhi-tasks-update',
  templateUrl: './tasks-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TasksUpdateComponent implements OnInit {
  isSaving = false;
  tasks: ITasks | null = null;

  editForm: TasksFormGroup = this.tasksFormService.createTasksFormGroup();

  constructor(
    protected tasksService: TasksService,
    protected tasksFormService: TasksFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tasks }) => {
      this.tasks = tasks;
      if (tasks) {
        this.updateForm(tasks);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tasks = this.tasksFormService.getTasks(this.editForm);
    if (tasks.id !== null) {
      this.subscribeToSaveResponse(this.tasksService.update(tasks));
    } else {
      this.subscribeToSaveResponse(this.tasksService.create(tasks));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITasks>>): void {
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

  protected updateForm(tasks: ITasks): void {
    this.tasks = tasks;
    this.tasksFormService.resetForm(this.editForm, tasks);
  }
}
