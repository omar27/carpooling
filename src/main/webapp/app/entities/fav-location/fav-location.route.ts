import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FavLocation } from 'app/shared/model/fav-location.model';
import { FavLocationService } from './fav-location.service';
import { FavLocationComponent } from './fav-location.component';
import { FavLocationDetailComponent } from './fav-location-detail.component';
import { FavLocationUpdateComponent } from './fav-location-update.component';
import { FavLocationDeletePopupComponent } from './fav-location-delete-dialog.component';
import { IFavLocation } from 'app/shared/model/fav-location.model';

@Injectable({ providedIn: 'root' })
export class FavLocationResolve implements Resolve<IFavLocation> {
  constructor(private service: FavLocationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFavLocation> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FavLocation>) => response.ok),
        map((favLocation: HttpResponse<FavLocation>) => favLocation.body)
      );
    }
    return of(new FavLocation());
  }
}

export const favLocationRoute: Routes = [
  {
    path: '',
    component: FavLocationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FavLocations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FavLocationDetailComponent,
    resolve: {
      favLocation: FavLocationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FavLocations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FavLocationUpdateComponent,
    resolve: {
      favLocation: FavLocationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FavLocations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FavLocationUpdateComponent,
    resolve: {
      favLocation: FavLocationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FavLocations'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const favLocationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FavLocationDeletePopupComponent,
    resolve: {
      favLocation: FavLocationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FavLocations'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
