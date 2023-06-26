import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FinishedPaymentFormService, FinishedPaymentFormGroup } from './finished-payment-form.service';
import { IFinishedPayment } from '../finished-payment.model';
import { FinishedPaymentService } from '../service/finished-payment.service';

@Component({
  standalone: true,
  selector: 'jhi-finished-payment-update',
  templateUrl: './finished-payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinishedPaymentUpdateComponent implements OnInit {
  isSaving = false;
  finishedPayment: IFinishedPayment | null = null;

  editForm: FinishedPaymentFormGroup = this.finishedPaymentFormService.createFinishedPaymentFormGroup();

  constructor(
    protected finishedPaymentService: FinishedPaymentService,
    protected finishedPaymentFormService: FinishedPaymentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ finishedPayment }) => {
      this.finishedPayment = finishedPayment;
      if (finishedPayment) {
        this.updateForm(finishedPayment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const finishedPayment = this.finishedPaymentFormService.getFinishedPayment(this.editForm);
    if (finishedPayment.id !== null) {
      this.subscribeToSaveResponse(this.finishedPaymentService.update(finishedPayment));
    } else {
      this.subscribeToSaveResponse(this.finishedPaymentService.create(finishedPayment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinishedPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(finishedPayment: IFinishedPayment): void {
    this.finishedPayment = finishedPayment;
    this.finishedPaymentFormService.resetForm(this.editForm, finishedPayment);
  }
}
