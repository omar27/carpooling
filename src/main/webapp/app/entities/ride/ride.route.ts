import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Ride } from 'app/shared/model/ride.model';
import { RideService } from './ride.service';
import { RideComponent } from './ride.component';
import { RideDetailComponent } from './ride-detail.component';
import { RideUpdateComponent } from './ride-update.component';
import { RideDeletePopupComponent } from './ride-delete-dialog.component';
import { IRide } from 'app/shared/model/ride.model';

@Injectable({ providedIn: 'root' })
export class RideResolve implements Resolve<IRide> {
  constructor(private service: RideService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRide> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Ride>) => response.ok),
        map((ride: HttpResponse<Ride>) => ride.body)
      );
    }
    return of(new Ride());
  }
}

export const rideRoute: Routes = [
  {
    path: '',
    component: RideComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Rides'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RideDetailComponent,
    resolve: {
      ride: RideResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER'],
      pageTitle: 'Rides'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RideUpdateComponent,
    resolve: {
      ride: RideResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'Rides'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RideUpdateComponent,
    resolve: {
      ride: RideResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Rides'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ridePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RideDeletePopupComponent,
    resolve: {
      ride: RideResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Rides'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
