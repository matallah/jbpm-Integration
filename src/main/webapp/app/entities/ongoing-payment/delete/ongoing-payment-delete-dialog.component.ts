import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOngoingPayment } from '../ongoing-payment.model';
import { OngoingPaymentService } from '../service/ongoing-payment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './ongoing-payment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OngoingPaymentDeleteDialogComponent {
  ongoingPayment?: IOngoingPayment;

  constructor(protected ongoingPaymentService: OngoingPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ongoingPaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
