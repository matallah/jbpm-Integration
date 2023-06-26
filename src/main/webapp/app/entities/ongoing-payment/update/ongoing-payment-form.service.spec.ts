import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ongoing-payment.test-samples';

import { OngoingPaymentFormService } from './ongoing-payment-form.service';

describe('OngoingPayment Form Service', () => {
  let service: OngoingPaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OngoingPaymentFormService);
  });

  describe('Service methods', () => {
    describe('createOngoingPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOngoingPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IOngoingPayment should create a new form with FormGroup', () => {
        const formGroup = service.createOngoingPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getOngoingPayment', () => {
      it('should return NewOngoingPayment for default OngoingPayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOngoingPaymentFormGroup(sampleWithNewData);

        const ongoingPayment = service.getOngoingPayment(formGroup) as any;

        expect(ongoingPayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOngoingPayment for empty OngoingPayment initial value', () => {
        const formGroup = service.createOngoingPaymentFormGroup();

        const ongoingPayment = service.getOngoingPayment(formGroup) as any;

        expect(ongoingPayment).toMatchObject({});
      });

      it('should return IOngoingPayment', () => {
        const formGroup = service.createOngoingPaymentFormGroup(sampleWithRequiredData);

        const ongoingPayment = service.getOngoingPayment(formGroup) as any;

        expect(ongoingPayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOngoingPayment should not enable id FormControl', () => {
        const formGroup = service.createOngoingPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOngoingPayment should disable id FormControl', () => {
        const formGroup = service.createOngoingPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
