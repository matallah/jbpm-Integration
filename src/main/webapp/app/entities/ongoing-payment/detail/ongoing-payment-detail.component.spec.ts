import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OngoingPaymentDetailComponent } from './ongoing-payment-detail.component';

describe('OngoingPayment Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OngoingPaymentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OngoingPaymentDetailComponent,
              resolve: { ongoingPayment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OngoingPaymentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ongoingPayment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OngoingPaymentDetailComponent);

      // THEN
      expect(instance.ongoingPayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
