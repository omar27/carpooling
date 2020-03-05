import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'car-pooling-user',
        loadChildren: () => import('./car-pooling-user/car-pooling-user.module').then(m => m.CarpoolingCarPoolingUserModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.CarpoolingLocationModule)
      },
      {
        path: 'ride',
        loadChildren: () => import('./ride/ride.module').then(m => m.CarpoolingRideModule)
      },
      {
        path: 'fav-location',
        loadChildren: () => import('./fav-location/fav-location.module').then(m => m.CarpoolingFavLocationModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CarpoolingEntityModule {}
