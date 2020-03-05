import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFavLocation, FavLocation } from 'app/shared/model/fav-location.model';
import { FavLocationService } from './fav-location.service';
import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { CarPoolingUserService } from 'app/entities/car-pooling-user/car-pooling-user.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-fav-location-update',
  templateUrl: './fav-location-update.component.html'
})
export class FavLocationUpdateComponent implements OnInit {
  isSaving: boolean;

  carpoolingusers: ICarPoolingUser[];

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    userId: [null, Validators.required],
    destinationId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected favLocationService: FavLocationService,
    protected carPoolingUserService: CarPoolingUserService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ favLocation }) => {
      this.updateForm(favLocation);
    });
    this.carPoolingUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICarPoolingUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICarPoolingUser[]>) => response.body)
      )
      .subscribe((res: ICarPoolingUser[]) => (this.carpoolingusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(favLocation: IFavLocation) {
    this.editForm.patchValue({
      id: favLocation.id,
      userId: favLocation.userId,
      destinationId: favLocation.destinationId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const favLocation = this.createFromForm();
    if (favLocation.id !== undefined) {
      this.subscribeToSaveResponse(this.favLocationService.update(favLocation));
    } else {
      this.subscribeToSaveResponse(this.favLocationService.create(favLocation));
    }
  }

  private createFromForm(): IFavLocation {
    return {
      ...new FavLocation(),
      id: this.editForm.get(['id']).value,
      userId: this.editForm.get(['userId']).value,
      destinationId: this.editForm.get(['destinationId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavLocation>>) {
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
