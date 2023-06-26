import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProcessInstances, NewProcessInstances } from '../process-instances.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcessInstances for edit and NewProcessInstancesFormGroupInput for create.
 */
type ProcessInstancesFormGroupInput = IProcessInstances | PartialWithRequiredKeyOf<NewProcessInstances>;

type ProcessInstancesFormDefaults = Pick<NewProcessInstances, 'id'>;

type ProcessInstancesFormGroupContent = {
  id: FormControl<IProcessInstances['id'] | NewProcessInstances['id']>;
  processInstanceId: FormControl<IProcessInstances['processInstanceId']>;
  processId: FormControl<IProcessInstances['processId']>;
  processName: FormControl<IProcessInstances['processName']>;
};

export type ProcessInstancesFormGroup = FormGroup<ProcessInstancesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcessInstancesFormService {
  createProcessInstancesFormGroup(processInstances: ProcessInstancesFormGroupInput = { id: null }): ProcessInstancesFormGroup {
    const processInstancesRawValue = {
      ...this.getFormDefaults(),
      ...processInstances,
    };
    return new FormGroup<ProcessInstancesFormGroupContent>({
      id: new FormControl(
        { value: processInstancesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      processInstanceId: new FormControl(processInstancesRawValue.processInstanceId),
      processId: new FormControl(processInstancesRawValue.processId),
      processName: new FormControl(processInstancesRawValue.processName),
    });
  }

  getProcessInstances(form: ProcessInstancesFormGroup): IProcessInstances | NewProcessInstances {
    return form.getRawValue() as IProcessInstances | NewProcessInstances;
  }

  resetForm(form: ProcessInstancesFormGroup, processInstances: ProcessInstancesFormGroupInput): void {
    const processInstancesRawValue = { ...this.getFormDefaults(), ...processInstances };
    form.reset(
      {
        ...processInstancesRawValue,
        id: { value: processInstancesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProcessInstancesFormDefaults {
    return {
      id: null,
    };
  }
}
