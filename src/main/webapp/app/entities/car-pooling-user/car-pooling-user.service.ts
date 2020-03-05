import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';

type EntityResponseType = HttpResponse<ICarPoolingUser>;
type EntityArrayResponseType = HttpResponse<ICarPoolingUser[]>;

@Injectable({ providedIn: 'root' })
export class CarPoolingUserService {
  public resourceUrl = SERVER_API_URL + 'api/car-pooling-users';

  constructor(protected http: HttpClient) {}

  create(carPoolingUser: ICarPoolingUser): Observable<EntityResponseType> {
    return this.http.post<ICarPoolingUser>(SERVER_API_URL + 'api/v2/register/user', carPoolingUser, { observe: 'response' });
  }

  update(carPoolingUser: ICarPoolingUser): Observable<EntityResponseType> {
    return this.http.put<ICarPoolingUser>(this.resourceUrl, carPoolingUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarPoolingUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarPoolingUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
