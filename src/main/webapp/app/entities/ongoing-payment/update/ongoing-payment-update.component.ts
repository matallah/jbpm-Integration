import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OngoingPaymentFormService, OngoingPaymentFormGroup } from './ongoing-payment-form.service';
import { IOngoingPayment } from '../ongoing-payment.model';
import { OngoingPaymentService } from '../service/ongoing-payment.service';

@Component({
  standalone: true,
  selector: 'jhi-ongoing-payment-update',
  templateUrl: './ongoing-payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OngoingPaymentUpdateComponent implements OnInit {
  isSaving = false;
  ongoingPayment: IOngoingPayment | null = null;

  editForm: OngoingPaymentFormGroup = this.ongoingPaymentFormService.createOngoingPaymentFormGroup();

  constructor(
    protected ongoingPaymentService: OngoingPaymentService,
    protected ongoingPaymentFormService: OngoingPaymentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ongoingPayment }) => {
      this.ongoingPayment = ongoingPayment;
      if (ongoingPayment) {
        this.updateForm(ongoingPayment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ongoingPayment = this.ongoingPaymentFormService.getOngoingPayment(this.editForm);
    if (ongoingPayment.id !== null) {
      this.subscribeToSaveResponse(this.ongoingPaymentService.update(ongoingPayment));
    } else {
      this.subscribeToSaveResponse(this.ongoingPaymentService.create(ongoingPayment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOngoingPayment>>): void {
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

  protected updateForm(ongoingPayment: IOngoingPayment): void {
    this.ongoingPayment = ongoingPayment;
    this.ongoingPaymentFormService.resetForm(this.editForm, ongoingPayment);
  }
}
