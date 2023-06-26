import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OngoingPaymentFormService } from './ongoing-payment-form.service';
import { OngoingPaymentService } from '../service/ongoing-payment.service';
import { IOngoingPayment } from '../ongoing-payment.model';

import { OngoingPaymentUpdateComponent } from './ongoing-payment-update.component';

describe('OngoingPayment Management Update Component', () => {
  let comp: OngoingPaymentUpdateComponent;
  let fixture: ComponentFixture<OngoingPaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ongoingPaymentFormService: OngoingPaymentFormService;
  let ongoingPaymentService: OngoingPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OngoingPaymentUpdateComponent],
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
      .overrideTemplate(OngoingPaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OngoingPaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ongoingPaymentFormService = TestBed.inject(OngoingPaymentFormService);
    ongoingPaymentService = TestBed.inject(OngoingPaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ongoingPayment: IOngoingPayment = { id: 456 };

      activatedRoute.data = of({ ongoingPayment });
      comp.ngOnInit();

      expect(comp.ongoingPayment).toEqual(ongoingPayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOngoingPayment>>();
      const ongoingPayment = { id: 123 };
      jest.spyOn(ongoingPaymentFormService, 'getOngoingPayment').mockReturnValue(ongoingPayment);
      jest.spyOn(ongoingPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ongoingPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ongoingPayment }));
      saveSubject.complete();

      // THEN
      expect(ongoingPaymentFormService.getOngoingPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ongoingPaymentService.update).toHaveBeenCalledWith(expect.objectContaining(ongoingPayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOngoingPayment>>();
      const ongoingPayment = { id: 123 };
      jest.spyOn(ongoingPaymentFormService, 'getOngoingPayment').mockReturnValue({ id: null });
      jest.spyOn(ongoingPaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ongoingPayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ongoingPayment }));
      saveSubject.complete();

      // THEN
      expect(ongoingPaymentFormService.getOngoingPayment).toHaveBeenCalled();
      expect(ongoingPaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOngoingPayment>>();
      const ongoingPayment = { id: 123 };
      jest.spyOn(ongoingPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ongoingPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ongoingPaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
