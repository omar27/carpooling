import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRide } from 'app/shared/model/ride.model';

type EntityResponseType = HttpResponse<IRide>;
type EntityArrayResponseType = HttpResponse<IRide[]>;

@Injectable({ providedIn: 'root' })
export class RideService {
  public resourceUrl = SERVER_API_URL + 'api/rides';

  constructor(protected http: HttpClient) {}

  create(ride: IRide): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ride);
    return this.http
      .post<IRide>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ride: IRide): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ride);
    return this.http
      .put<IRide>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRide>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRide[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ride: IRide): IRide {
    const copy: IRide = Object.assign({}, ride, {
      date: ride.date != null && ride.date.isValid() ? ride.date.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ride: IRide) => {
        ride.date = ride.date != null ? moment(ride.date) : null;
      });
    }
    return res;
  }
}
