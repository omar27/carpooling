import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { CarPoolingUserService } from './car-pooling-user.service';
import { CarPoolingUserComponent } from './car-pooling-user.component';
import { CarPoolingUserDetailComponent } from './car-pooling-user-detail.component';
import { CarPoolingUserUpdateComponent } from './car-pooling-user-update.component';
import { CarPoolingUserDeletePopupComponent } from './car-pooling-user-delete-dialog.component';
import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';

@Injectable({ providedIn: 'root' })
export class CarPoolingUserResolve implements Resolve<ICarPoolingUser> {
  constructor(private service: CarPoolingUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICarPoolingUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CarPoolingUser>) => response.ok),
        map((carPoolingUser: HttpResponse<CarPoolingUser>) => carPoolingUser.body)
      );
    }
    return of(new CarPoolingUser());
  }
}

export const carPoolingUserRoute: Routes = [
  {
    path: '',
    component: CarPoolingUserComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'CarPoolingUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CarPoolingUserDetailComponent,
    resolve: {
      carPoolingUser: CarPoolingUserResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER'],
      pageTitle: 'CarPoolingUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CarPoolingUserUpdateComponent,
    resolve: {
      carPoolingUser: CarPoolingUserResolve
    },
    data: {
      authorities: [''],
      pageTitle: 'CarPoolingUsers'
    },
    canActivate: []
  },
  {
    path: ':id/edit',
    component: CarPoolingUserUpdateComponent,
    resolve: {
      carPoolingUser: CarPoolingUserResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'CarPoolingUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const carPoolingUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CarPoolingUserDeletePopupComponent,
    resolve: {
      carPoolingUser: CarPoolingUserResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'CarPoolingUsers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
