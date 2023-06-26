import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IProcessInstances } from '../process-instances.model';
import { ProcessInstancesService } from '../service/process-instances.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './process-instances-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProcessInstancesDeleteDialogComponent {
  processInstances?: IProcessInstances;

  constructor(protected processInstancesService: ProcessInstancesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.processInstancesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
