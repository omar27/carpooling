import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICarPoolingUser, CarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { CarPoolingUserService } from './car-pooling-user.service';

@Component({
  selector: 'jhi-car-pooling-user-update',
  templateUrl: './car-pooling-user-update.component.html'
})
export class CarPoolingUserUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required, Validators.minLength(11)]],
    password: [null, [Validators.required, Validators.minLength(4)]],
    type: []
  });

  constructor(protected carPoolingUserService: CarPoolingUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ carPoolingUser }) => {
      this.updateForm(carPoolingUser);
    });
  }

  updateForm(carPoolingUser: ICarPoolingUser) {
    this.editForm.patchValue({
      id: carPoolingUser.id,
      firstName: carPoolingUser.firstName,
      lastName: carPoolingUser.lastName,
      email: carPoolingUser.email,
      phoneNumber: carPoolingUser.phoneNumber,
      password: carPoolingUser.password,
      type: carPoolingUser.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const carPoolingUser = this.createFromForm();
    if (carPoolingUser.id !== undefined) {
      this.subscribeToSaveResponse(this.carPoolingUserService.update(carPoolingUser));
    } else {
      this.subscribeToSaveResponse(this.carPoolingUserService.create(carPoolingUser));
    }
  }

  private createFromForm(): ICarPoolingUser {
    return {
      ...new CarPoolingUser(),
      id: this.editForm.get(['id']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      email: this.editForm.get(['email']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      password: this.editForm.get(['password']).value,
      type: this.editForm.get(['type']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarPoolingUser>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
