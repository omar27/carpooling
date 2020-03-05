import { Injectable } from '@angular/core';
import { Observable, Observer, Subscription } from 'rxjs';

import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';
import { Router } from '@angular/router';
import { HttpService } from './http.service';

@Injectable({ providedIn: 'root' })
export class Websockets {
  stompClient = null;
  subscriber = null;
  connection: Promise<any>;
  connectedPromise: any;
  listener: Observable<any>;
  listenerObserver: Observer<any>;
  alreadyConnectedOnce = false;
  private subscription: Subscription;
  notifications: any;

  constructor(private httpService: HttpService, private router: Router) {
    // this.connection = this.createConnection();
    // this.listener = this.createListener();
    // this.listenSocket();
  }
  connectWebsockets() {
    const socket = new SockJS(`http://localhost:8080/websocket`);
    const stompClient = Stomp.over(socket);
    return stompClient;
  }

  listenSocket() {
    this.httpService.sendData(localStorage.getItem('email'));
    const stompClient = this.connectWebsockets();

    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/notifications', notifications => {
        this.notifications = notifications.body;
      });
    });
  }
}
