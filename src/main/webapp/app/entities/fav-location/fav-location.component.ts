import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IFavLocation } from 'app/shared/model/fav-location.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FavLocationService } from './fav-location.service';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

@Component({
  selector: 'jhi-fav-location',
  templateUrl: './fav-location.component.html'
})
export class FavLocationComponent implements OnInit, OnDestroy {
  favLocations: IFavLocation[];
  currentAccount: any;
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;
  userType: string;

  userSpecificFavLocations: IFavLocation[];

  constructor(
    protected favLocationService: FavLocationService,
    protected eventManager: JhiEventManager,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService
  ) {
    this.favLocations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
    this.userType = 'ADMIN';
    this.listenSocket();
  }

  loadAll() {
    this.favLocationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFavLocation[]>) => this.paginateFavLocations(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.favLocations = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
      this.userType = localStorage.getItem('userType');
      if (account.authorities.length > 1) {
        this.userType = 'ADMIN';
        localStorage.setItem('userType', this.userType);
      }
    });
    this.loadAll();
    this.registerChangeInFavLocations();
  }
  connectWebsockets() {
    const socket = new SockJS(`http://localhost:8080/websocket`);
    const stompClient = Stomp.over(socket);
    return stompClient;
  }

  listenSocket() {
    const stompClient = this.connectWebsockets();

    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/locations', notifications => {
        this.userSpecificFavLocations = [];
        this.userSpecificFavLocations = JSON.parse(notifications.body);
        if (localStorage.getItem('userType') !== 'ADMIN') {
          this.favLocations = this.userSpecificFavLocations;
        }
      });
    });
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFavLocation) {
    return item.id;
  }

  registerChangeInFavLocations() {
    this.eventSubscriber = this.eventManager.subscribe('favLocationListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFavLocations(data: IFavLocation[], headers: HttpHeaders) {
    if (localStorage.getItem('userType') !== 'ADMIN') {
      return;
    }
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.favLocations.push(data[i]);
    }
  }
}
