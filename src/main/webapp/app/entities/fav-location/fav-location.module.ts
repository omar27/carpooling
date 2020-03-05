import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CarpoolingSharedModule } from 'app/shared/shared.module';
import { FavLocationComponent } from './fav-location.component';
import { FavLocationDetailComponent } from './fav-location-detail.component';
import { FavLocationUpdateComponent } from './fav-location-update.component';
import { FavLocationDeletePopupComponent, FavLocationDeleteDialogComponent } from './fav-location-delete-dialog.component';
import { favLocationRoute, favLocationPopupRoute } from './fav-location.route';

const ENTITY_STATES = [...favLocationRoute, ...favLocationPopupRoute];

@NgModule({
  imports: [CarpoolingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FavLocationComponent,
    FavLocationDetailComponent,
    FavLocationUpdateComponent,
    FavLocationDeleteDialogComponent,
    FavLocationDeletePopupComponent
  ],
  entryComponents: [FavLocationDeleteDialogComponent]
})
export class CarpoolingFavLocationModule {}
