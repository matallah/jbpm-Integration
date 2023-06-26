import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../finished-payment.test-samples';

import { FinishedPaymentFormService } from './finished-payment-form.service';

describe('FinishedPayment Form Service', () => {
  let service: FinishedPaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinishedPaymentFormService);
  });

  describe('Service methods', () => {
    describe('createFinishedPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinishedPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            name: expect.any(Object),
            approve: expect.any(Object),
          })
        );
      });

      it('passing IFinishedPayment should create a new form with FormGroup', () => {
        const formGroup = service.createFinishedPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            name: expect.any(Object),
            approve: expect.any(Object),
          })
        );
      });
    });

    describe('getFinishedPayment', () => {
      it('should return NewFinishedPayment for default FinishedPayment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFinishedPaymentFormGroup(sampleWithNewData);

        const finishedPayment = service.getFinishedPayment(formGroup) as any;

        expect(finishedPayment).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinishedPayment for empty FinishedPayment initial value', () => {
        const formGroup = service.createFinishedPaymentFormGroup();

        const finishedPayment = service.getFinishedPayment(formGroup) as any;

        expect(finishedPayment).toMatchObject({});
      });

      it('should return IFinishedPayment', () => {
        const formGroup = service.createFinishedPaymentFormGroup(sampleWithRequiredData);

        const finishedPayment = service.getFinishedPayment(formGroup) as any;

        expect(finishedPayment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinishedPayment should not enable id FormControl', () => {
        const formGroup = service.createFinishedPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinishedPayment should disable id FormControl', () => {
        const formGroup = service.createFinishedPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
