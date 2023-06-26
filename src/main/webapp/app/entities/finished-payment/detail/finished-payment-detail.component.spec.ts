import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinishedPaymentDetailComponent } from './finished-payment-detail.component';

describe('FinishedPayment Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinishedPaymentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FinishedPaymentDetailComponent,
              resolve: { finishedPayment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(FinishedPaymentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load finishedPayment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FinishedPaymentDetailComponent);

      // THEN
      expect(instance.finishedPayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
