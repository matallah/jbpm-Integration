import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFinishedPayment, NewFinishedPayment } from '../finished-payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinishedPayment for edit and NewFinishedPaymentFormGroupInput for create.
 */
type FinishedPaymentFormGroupInput = IFinishedPayment | PartialWithRequiredKeyOf<NewFinishedPayment>;

type FinishedPaymentFormDefaults = Pick<NewFinishedPayment, 'id' | 'approve'>;

type FinishedPaymentFormGroupContent = {
  id: FormControl<IFinishedPayment['id'] | NewFinishedPayment['id']>;
  price: FormControl<IFinishedPayment['price']>;
  name: FormControl<IFinishedPayment['name']>;
  approve: FormControl<IFinishedPayment['approve']>;
};

export type FinishedPaymentFormGroup = FormGroup<FinishedPaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinishedPaymentFormService {
  createFinishedPaymentFormGroup(finishedPayment: FinishedPaymentFormGroupInput = { id: null }): FinishedPaymentFormGroup {
    const finishedPaymentRawValue = {
      ...this.getFormDefaults(),
      ...finishedPayment,
    };
    return new FormGroup<FinishedPaymentFormGroupContent>({
      id: new FormControl(
        { value: finishedPaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      price: new FormControl(finishedPaymentRawValue.price),
      name: new FormControl(finishedPaymentRawValue.name),
      approve: new FormControl(finishedPaymentRawValue.approve),
    });
  }

  getFinishedPayment(form: FinishedPaymentFormGroup): IFinishedPayment | NewFinishedPayment {
    return form.getRawValue() as IFinishedPayment | NewFinishedPayment;
  }

  resetForm(form: FinishedPaymentFormGroup, finishedPayment: FinishedPaymentFormGroupInput): void {
    const finishedPaymentRawValue = { ...this.getFormDefaults(), ...finishedPayment };
    form.reset(
      {
        ...finishedPaymentRawValue,
        id: { value: finishedPaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FinishedPaymentFormDefaults {
    return {
      id: null,
      approve: false,
    };
  }
}
