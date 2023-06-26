import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinishedPaymentService } from '../service/finished-payment.service';

import { FinishedPaymentComponent } from './finished-payment.component';

describe('FinishedPayment Management Component', () => {
  let comp: FinishedPaymentComponent;
  let fixture: ComponentFixture<FinishedPaymentComponent>;
  let service: FinishedPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'finished-payment', component: FinishedPaymentComponent }]),
        HttpClientTestingModule,
        FinishedPaymentComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FinishedPaymentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinishedPaymentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FinishedPaymentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.finishedPayments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to finishedPaymentService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFinishedPaymentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFinishedPaymentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
