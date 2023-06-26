import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProcessInstancesFormService } from './process-instances-form.service';
import { ProcessInstancesService } from '../service/process-instances.service';
import { IProcessInstances } from '../process-instances.model';

import { ProcessInstancesUpdateComponent } from './process-instances-update.component';

describe('ProcessInstances Management Update Component', () => {
  let comp: ProcessInstancesUpdateComponent;
  let fixture: ComponentFixture<ProcessInstancesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let processInstancesFormService: ProcessInstancesFormService;
  let processInstancesService: ProcessInstancesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProcessInstancesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProcessInstancesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessInstancesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    processInstancesFormService = TestBed.inject(ProcessInstancesFormService);
    processInstancesService = TestBed.inject(ProcessInstancesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const processInstances: IProcessInstances = { id: 456 };

      activatedRoute.data = of({ processInstances });
      comp.ngOnInit();

      expect(comp.processInstances).toEqual(processInstances);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessInstances>>();
      const processInstances = { id: 123 };
      jest.spyOn(processInstancesFormService, 'getProcessInstances').mockReturnValue(processInstances);
      jest.spyOn(processInstancesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processInstances });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processInstances }));
      saveSubject.complete();

      // THEN
      expect(processInstancesFormService.getProcessInstances).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(processInstancesService.update).toHaveBeenCalledWith(expect.objectContaining(processInstances));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessInstances>>();
      const processInstances = { id: 123 };
      jest.spyOn(processInstancesFormService, 'getProcessInstances').mockReturnValue({ id: null });
      jest.spyOn(processInstancesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processInstances: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processInstances }));
      saveSubject.complete();

      // THEN
      expect(processInstancesFormService.getProcessInstances).toHaveBeenCalled();
      expect(processInstancesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessInstances>>();
      const processInstances = { id: 123 };
      jest.spyOn(processInstancesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processInstances });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(processInstancesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
