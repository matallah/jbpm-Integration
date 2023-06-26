import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tasks.test-samples';

import { TasksFormService } from './tasks-form.service';

describe('Tasks Form Service', () => {
  let service: TasksFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TasksFormService);
  });

  describe('Service methods', () => {
    describe('createTasksFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTasksFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            taskId: expect.any(Object),
            taskName: expect.any(Object),
            taskStatus: expect.any(Object),
            price: expect.any(Object),
            approve: expect.any(Object),
          })
        );
      });

      it('passing ITasks should create a new form with FormGroup', () => {
        const formGroup = service.createTasksFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            taskId: expect.any(Object),
            taskName: expect.any(Object),
            taskStatus: expect.any(Object),
            price: expect.any(Object),
            approve: expect.any(Object),
          })
        );
      });
    });

    describe('getTasks', () => {
      it('should return NewTasks for default Tasks initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTasksFormGroup(sampleWithNewData);

        const tasks = service.getTasks(formGroup) as any;

        expect(tasks).toMatchObject(sampleWithNewData);
      });

      it('should return NewTasks for empty Tasks initial value', () => {
        const formGroup = service.createTasksFormGroup();

        const tasks = service.getTasks(formGroup) as any;

        expect(tasks).toMatchObject({});
      });

      it('should return ITasks', () => {
        const formGroup = service.createTasksFormGroup(sampleWithRequiredData);

        const tasks = service.getTasks(formGroup) as any;

        expect(tasks).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITasks should not enable id FormControl', () => {
        const formGroup = service.createTasksFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTasks should disable id FormControl', () => {
        const formGroup = service.createTasksFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
