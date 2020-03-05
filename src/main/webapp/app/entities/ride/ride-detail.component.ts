import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRide } from 'app/shared/model/ride.model';

@Component({
  selector: 'jhi-ride-detail',
  templateUrl: './ride-detail.component.html'
})
export class RideDetailComponent implements OnInit {
  ride: IRide;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ride }) => {
      this.ride = ride;
    });
  }

  previousState() {
    window.history.back();
  }
}
