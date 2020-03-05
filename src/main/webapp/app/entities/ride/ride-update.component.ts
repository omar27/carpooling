import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IRide, Ride } from 'app/shared/model/ride.model';
import { RideService } from './ride.service';
import { ICarPoolingUser, CarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { CarPoolingUserService } from 'app/entities/car-pooling-user/car-pooling-user.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { LogsModule } from 'app/admin/logs/logs.module';

@Component({
  selector: 'jhi-ride-update',
  templateUrl: './ride-update.component.html'
})
export class RideUpdateComponent implements OnInit {
  isSaving: boolean;
  authority: String;
  userName: String;
  userId: Number;
  carpoolingusers: ICarPoolingUser[];

  locations: ILocation[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    status: [null, [Validators.required]],
    driverId: [null, Validators.required],
    passengerId: [],
    sourceId: [null, Validators.required],
    destinationId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rideService: RideService,
    protected carPoolingUserService: CarPoolingUserService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.authority = localStorage.getItem('authorities').split(',').length > 1 ? 'ROLE_ADMIN' : 'ROLE_USER';
    this.userName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastName');
    this.activatedRoute.data.subscribe(({ ride }) => {
      this.updateForm(ride);
    });
    this.carPoolingUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICarPoolingUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICarPoolingUser[]>) => response.body)
      )
      .subscribe((res: ICarPoolingUser[]) => this.onCarPoolUserRecieved(res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  onCarPoolUserRecieved(response: ICarPoolingUser[]) {
    this.carpoolingusers = response;
    response.forEach(element => {
      if (element.firstName + ' ' + element.lastName === this.userName) {
        this.userId = element.id;
      }
    });
  }

  updateForm(ride: IRide) {
    this.editForm.patchValue({
      id: ride.id,
      date: ride.date,
      status: ride.status,
      driverId: ride.driverId,
      passengerId: ride.passengerId,
      sourceId: ride.sourceId,
      destinationId: ride.destinationId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ride = this.createFromForm();
    if (ride.id !== undefined) {
      this.subscribeToSaveResponse(this.rideService.update(ride));
    } else {
      this.subscribeToSaveResponse(this.rideService.create(ride));
    }
  }

  private createFromForm(): IRide {
    return {
      ...new Ride(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value,
      status: this.editForm.get(['status']).value,
      driverId: this.editForm.get(['driverId']).value,
      passengerId: this.editForm.get(['passengerId']).value,
      sourceId: this.editForm.get(['sourceId']).value,
      destinationId: this.editForm.get(['destinationId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRide>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCarPoolingUserById(index: number, item: ICarPoolingUser) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
