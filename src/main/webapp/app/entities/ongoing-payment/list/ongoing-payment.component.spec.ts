import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OngoingPaymentService } from '../service/ongoing-payment.service';

import { OngoingPaymentComponent } from './ongoing-payment.component';

describe('OngoingPayment Management Component', () => {
  let comp: OngoingPaymentComponent;
  let fixture: ComponentFixture<OngoingPaymentComponent>;
  let service: OngoingPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'ongoing-payment', component: OngoingPaymentComponent }]),
        HttpClientTestingModule,
        OngoingPaymentComponent,
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
      .overrideTemplate(OngoingPaymentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OngoingPaymentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OngoingPaymentService);

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
    expect(comp.ongoingPayments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ongoingPaymentService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOngoingPaymentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOngoingPaymentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
