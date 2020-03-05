import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFavLocation } from 'app/shared/model/fav-location.model';

type EntityResponseType = HttpResponse<IFavLocation>;
type EntityArrayResponseType = HttpResponse<IFavLocation[]>;

@Injectable({ providedIn: 'root' })
export class FavLocationService {
  public resourceUrl = SERVER_API_URL + 'api/fav-locations';

  constructor(protected http: HttpClient) {}

  create(favLocation: IFavLocation): Observable<EntityResponseType> {
    return this.http.post<IFavLocation>(this.resourceUrl, favLocation, { observe: 'response' });
  }

  update(favLocation: IFavLocation): Observable<EntityResponseType> {
    return this.http.put<IFavLocation>(this.resourceUrl, favLocation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
