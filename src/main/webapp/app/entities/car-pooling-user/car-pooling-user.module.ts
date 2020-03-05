import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CarpoolingSharedModule } from 'app/shared/shared.module';
import { CarPoolingUserComponent } from './car-pooling-user.component';
import { CarPoolingUserDetailComponent } from './car-pooling-user-detail.component';
import { CarPoolingUserUpdateComponent } from './car-pooling-user-update.component';
import { CarPoolingUserDeletePopupComponent, CarPoolingUserDeleteDialogComponent } from './car-pooling-user-delete-dialog.component';
import { carPoolingUserRoute, carPoolingUserPopupRoute } from './car-pooling-user.route';

const ENTITY_STATES = [...carPoolingUserRoute, ...carPoolingUserPopupRoute];

@NgModule({
  imports: [CarpoolingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CarPoolingUserComponent,
    CarPoolingUserDetailComponent,
    CarPoolingUserUpdateComponent,
    CarPoolingUserDeleteDialogComponent,
    CarPoolingUserDeletePopupComponent
  ],
  entryComponents: [CarPoolingUserDeleteDialogComponent]
})
export class CarpoolingCarPoolingUserModule {}
