import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../process-instances.test-samples';

import { ProcessInstancesFormService } from './process-instances-form.service';

describe('ProcessInstances Form Service', () => {
  let service: ProcessInstancesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcessInstancesFormService);
  });

  describe('Service methods', () => {
    describe('createProcessInstancesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProcessInstancesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            processInstanceId: expect.any(Object),
            processId: expect.any(Object),
            processName: expect.any(Object),
          })
        );
      });

      it('passing IProcessInstances should create a new form with FormGroup', () => {
        const formGroup = service.createProcessInstancesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            processInstanceId: expect.any(Object),
            processId: expect.any(Object),
            processName: expect.any(Object),
          })
        );
      });
    });

    describe('getProcessInstances', () => {
      it('should return NewProcessInstances for default ProcessInstances initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProcessInstancesFormGroup(sampleWithNewData);

        const processInstances = service.getProcessInstances(formGroup) as any;

        expect(processInstances).toMatchObject(sampleWithNewData);
      });

      it('should return NewProcessInstances for empty ProcessInstances initial value', () => {
        const formGroup = service.createProcessInstancesFormGroup();

        const processInstances = service.getProcessInstances(formGroup) as any;

        expect(processInstances).toMatchObject({});
      });

      it('should return IProcessInstances', () => {
        const formGroup = service.createProcessInstancesFormGroup(sampleWithRequiredData);

        const processInstances = service.getProcessInstances(formGroup) as any;

        expect(processInstances).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProcessInstances should not enable id FormControl', () => {
        const formGroup = service.createProcessInstancesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProcessInstances should disable id FormControl', () => {
        const formGroup = service.createProcessInstancesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
