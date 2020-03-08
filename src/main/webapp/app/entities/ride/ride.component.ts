import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IRide, Ride } from 'app/shared/model/ride.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RideService } from './ride.service';
import { CarPoolingUserService } from '../car-pooling-user/car-pooling-user.service';
import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { Account } from 'app/core/user/account.model';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';
import { Status } from 'app/shared/model/enumerations/status.model';

@Component({
  selector: 'jhi-ride',
  templateUrl: './ride.component.html'
})
export class RideComponent implements OnInit, OnDestroy {
  rides: Ride[];
  currentAccount: Account;
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;
  isAdmin = false;
  carpoolingusers: ICarPoolingUser[];
  currentCarpoolUser: ICarPoolingUser;
  favRides: Ride[];
  bookedRides: Ride[];
  userType: string;
  updateRide: Ride;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rideService: RideService,
    protected eventManager: JhiEventManager,
    protected parseLinks: JhiParseLinks,
    protected carPoolingUserService: CarPoolingUserService,
    protected accountService: AccountService
  ) {
    this.rides = [];
    this.favRides = [];
    this.bookedRides = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
    this.updateRide = new Ride();
  }

  loadAll() {
    this.rides = [];
    this.rideService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IRide[]>) => this.paginateRides(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.rides = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe((account: Account) => {
      this.currentAccount = account;
      this.userType = localStorage.getItem('userType');
      if (account.authorities.length > 1) {
        this.isAdmin = true;
        this.userType = 'ADMIN';
        localStorage.setItem('userType', this.userType);
      }
    });
    this.carPoolingUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICarPoolingUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICarPoolingUser[]>) => response.body)
      )
      .subscribe(
        (res: ICarPoolingUser[]) => {
          for (const user of res) {
            if (user.email === this.currentAccount.email) {
              this.currentCarpoolUser = user;
            }
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.registerChangeInRides();
    this.listenSocket();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRide) {
    return item.id;
  }

  bookRide(ride: any) {
    this.updateRide.id = ride.id;
    this.updateRide.date = ride.date;
    this.updateRide.status = Status.BOOKED;
    this.updateRide.destinationCity = ride.destination.city;
    this.updateRide.destinationId = ride.destination.id;
    this.updateRide.driverFirstName = ride.driver.firstName;
    this.updateRide.driverId = ride.driver.id;
    this.updateRide.passengerFirstName = this.currentCarpoolUser.firstName;
    this.updateRide.passengerId = this.currentCarpoolUser.id;
    this.updateRide.sourceCity = ride.source.city;
    this.updateRide.sourceId = ride.source.id;
    this.subscribeToSaveResponse(this.rideService.update(this.updateRide));
  }

  registerChangeInRides() {
    this.eventSubscriber = this.eventManager.subscribe('rideListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRide>>) {
    result.subscribe(() => this.onSaveSuccess(result), () => this.onSaveError(result));
  }

  protected onSaveSuccess(result: any) {
    const result1 = result;
  }

  connectWebsockets() {
    const socket = new SockJS(`http://localhost:8080/websocket`);
    const stompClient = Stomp.over(socket);
    return stompClient;
  }

  listenSocket() {
    const stompClient = this.connectWebsockets();

    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/passenger', notifications => {
        this.favRides = [];
        this.favRides = JSON.parse(notifications.body);
      });

      stompClient.subscribe('/topic/driver', notifications => {
        this.bookedRides = [];
        this.bookedRides = JSON.parse(notifications.body);
      });
    });
  }

  protected onSaveError(result: any) {
    const result1 = result;
  }

  protected paginateRides(data: IRide[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.rides.push(data[i]);
    }
  }
}
