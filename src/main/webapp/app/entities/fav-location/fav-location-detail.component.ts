import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFavLocation } from 'app/shared/model/fav-location.model';

@Component({
  selector: 'jhi-fav-location-detail',
  templateUrl: './fav-location-detail.component.html'
})
export class FavLocationDetailComponent implements OnInit {
  favLocation: IFavLocation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ favLocation }) => {
      this.favLocation = favLocation;
    });
  }

  previousState() {
    window.history.back();
  }
}
