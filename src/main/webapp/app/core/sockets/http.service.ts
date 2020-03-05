import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HttpService {
  public resourceUrl = 'http://localhost:8080/data';

  constructor(protected http: HttpClient) {}

  sendData(email: string): Observable<any> {
    return this.http.post<any>(this.resourceUrl, email, { observe: 'response' });
  }
}
