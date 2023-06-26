import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOngoingPayment } from '../ongoing-payment.model';

@Component({
  standalone: true,
  selector: 'jhi-ongoing-payment-detail',
  templateUrl: './ongoing-payment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OngoingPaymentDetailComponent {
  @Input() ongoingPayment: IOngoingPayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
