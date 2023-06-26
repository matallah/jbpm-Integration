import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOngoingPayment, NewOngoingPayment } from '../ongoing-payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOngoingPayment for edit and NewOngoingPaymentFormGroupInput for create.
 */
type OngoingPaymentFormGroupInput = IOngoingPayment | PartialWithRequiredKeyOf<NewOngoingPayment>;

type OngoingPaymentFormDefaults = Pick<NewOngoingPayment, 'id'>;

type OngoingPaymentFormGroupContent = {
  id: FormControl<IOngoingPayment['id'] | NewOngoingPayment['id']>;
  price: FormControl<IOngoingPayment['price']>;
  name: FormControl<IOngoingPayment['name']>;
};

export type OngoingPaymentFormGroup = FormGroup<OngoingPaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OngoingPaymentFormService {
  createOngoingPaymentFormGroup(ongoingPayment: OngoingPaymentFormGroupInput = { id: null }): OngoingPaymentFormGroup {
    const ongoingPaymentRawValue = {
      ...this.getFormDefaults(),
      ...ongoingPayment,
    };
    return new FormGroup<OngoingPaymentFormGroupContent>({
      id: new FormControl(
        { value: ongoingPaymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      price: new FormControl(ongoingPaymentRawValue.price),
      name: new FormControl(ongoingPaymentRawValue.name),
    });
  }

  getOngoingPayment(form: OngoingPaymentFormGroup): IOngoingPayment | NewOngoingPayment {
    return form.getRawValue() as IOngoingPayment | NewOngoingPayment;
  }

  resetForm(form: OngoingPaymentFormGroup, ongoingPayment: OngoingPaymentFormGroupInput): void {
    const ongoingPaymentRawValue = { ...this.getFormDefaults(), ...ongoingPayment };
    form.reset(
      {
        ...ongoingPaymentRawValue,
        id: { value: ongoingPaymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OngoingPaymentFormDefaults {
    return {
      id: null,
    };
  }
}
