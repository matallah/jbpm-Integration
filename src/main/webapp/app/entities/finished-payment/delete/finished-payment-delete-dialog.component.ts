import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IFinishedPayment } from '../finished-payment.model';
import { FinishedPaymentService } from '../service/finished-payment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './finished-payment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinishedPaymentDeleteDialogComponent {
  finishedPayment?: IFinishedPayment;

  constructor(protected finishedPaymentService: FinishedPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.finishedPaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
