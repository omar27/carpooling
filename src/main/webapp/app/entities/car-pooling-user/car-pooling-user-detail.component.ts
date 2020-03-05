import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';

@Component({
  selector: 'jhi-car-pooling-user-detail',
  templateUrl: './car-pooling-user-detail.component.html'
})
export class CarPoolingUserDetailComponent implements OnInit {
  carPoolingUser: ICarPoolingUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ carPoolingUser }) => {
      this.carPoolingUser = carPoolingUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
