import { Moment } from 'moment';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IRide {
  id?: number;
  date?: Moment;
  status?: Status;
  driverFirstName?: string;
  driverId?: number;
  passengerFirstName?: string;
  passengerId?: number;
  sourceCity?: string;
  sourceId?: number;
  destinationCity?: string;
  destinationId?: number;
}

export class Ride implements IRide {
  constructor(
    public id?: number,
    public date?: Moment,
    public status?: Status,
    public driverFirstName?: string,
    public driverId?: number,
    public passengerFirstName?: string,
    public passengerId?: number,
    public sourceCity?: string,
    public sourceId?: number,
    public destinationCity?: string,
    public destinationId?: number
  ) {}
}
