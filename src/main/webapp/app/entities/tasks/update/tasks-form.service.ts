import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITasks, NewTasks } from '../tasks.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITasks for edit and NewTasksFormGroupInput for create.
 */
type TasksFormGroupInput = ITasks | PartialWithRequiredKeyOf<NewTasks>;

type TasksFormDefaults = Pick<NewTasks, 'id' | 'approve'>;

type TasksFormGroupContent = {
  id: FormControl<ITasks['id'] | NewTasks['id']>;
  taskId: FormControl<ITasks['taskId']>;
  taskName: FormControl<ITasks['taskName']>;
  taskStatus: FormControl<ITasks['taskStatus']>;
  price: FormControl<ITasks['price']>;
  approve: FormControl<ITasks['approve']>;
  name: FormControl<ITasks['name']>;
};

export type TasksFormGroup = FormGroup<TasksFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TasksFormService {
  createTasksFormGroup(tasks: TasksFormGroupInput = { id: null }): TasksFormGroup {
    const tasksRawValue = {
      ...this.getFormDefaults(),
      ...tasks,
    };
    return new FormGroup<TasksFormGroupContent>({
      id: new FormControl(
        { value: tasksRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      taskId: new FormControl(tasksRawValue.taskId),
      taskName: new FormControl(tasksRawValue.taskName),
      taskStatus: new FormControl(tasksRawValue.taskStatus),
      price: new FormControl(tasksRawValue.price),
      approve: new FormControl(tasksRawValue.approve),
      name: new FormControl(tasksRawValue.name),
    });
  }

  getTasks(form: TasksFormGroup): ITasks | NewTasks {
    return form.getRawValue() as ITasks | NewTasks;
  }

  resetForm(form: TasksFormGroup, tasks: TasksFormGroupInput): void {
    const tasksRawValue = { ...this.getFormDefaults(), ...tasks };
    form.reset(
      {
        ...tasksRawValue,
        id: { value: tasksRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TasksFormDefaults {
    return {
      id: null,
      approve: false,
    };
  }
}
