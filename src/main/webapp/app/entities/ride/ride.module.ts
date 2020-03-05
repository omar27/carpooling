import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CarpoolingSharedModule } from 'app/shared/shared.module';
import { RideComponent } from './ride.component';
import { RideDetailComponent } from './ride-detail.component';
import { RideUpdateComponent } from './ride-update.component';
import { RideDeletePopupComponent, RideDeleteDialogComponent } from './ride-delete-dialog.component';
import { rideRoute, ridePopupRoute } from './ride.route';

const ENTITY_STATES = [...rideRoute, ...ridePopupRoute];

@NgModule({
  imports: [CarpoolingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [RideComponent, RideDetailComponent, RideUpdateComponent, RideDeleteDialogComponent, RideDeletePopupComponent],
  entryComponents: [RideDeleteDialogComponent]
})
export class CarpoolingRideModule {}
