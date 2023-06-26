import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FinishedPaymentFormService } from './finished-payment-form.service';
import { FinishedPaymentService } from '../service/finished-payment.service';
import { IFinishedPayment } from '../finished-payment.model';

import { FinishedPaymentUpdateComponent } from './finished-payment-update.component';

describe('FinishedPayment Management Update Component', () => {
  let comp: FinishedPaymentUpdateComponent;
  let fixture: ComponentFixture<FinishedPaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let finishedPaymentFormService: FinishedPaymentFormService;
  let finishedPaymentService: FinishedPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FinishedPaymentUpdateComponent],
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
      .overrideTemplate(FinishedPaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinishedPaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    finishedPaymentFormService = TestBed.inject(FinishedPaymentFormService);
    finishedPaymentService = TestBed.inject(FinishedPaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const finishedPayment: IFinishedPayment = { id: 456 };

      activatedRoute.data = of({ finishedPayment });
      comp.ngOnInit();

      expect(comp.finishedPayment).toEqual(finishedPayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinishedPayment>>();
      const finishedPayment = { id: 123 };
      jest.spyOn(finishedPaymentFormService, 'getFinishedPayment').mockReturnValue(finishedPayment);
      jest.spyOn(finishedPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finishedPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finishedPayment }));
      saveSubject.complete();

      // THEN
      expect(finishedPaymentFormService.getFinishedPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(finishedPaymentService.update).toHaveBeenCalledWith(expect.objectContaining(finishedPayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinishedPayment>>();
      const finishedPayment = { id: 123 };
      jest.spyOn(finishedPaymentFormService, 'getFinishedPayment').mockReturnValue({ id: null });
      jest.spyOn(finishedPaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finishedPayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finishedPayment }));
      saveSubject.complete();

      // THEN
      expect(finishedPaymentFormService.getFinishedPayment).toHaveBeenCalled();
      expect(finishedPaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinishedPayment>>();
      const finishedPayment = { id: 123 };
      jest.spyOn(finishedPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finishedPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(finishedPaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
