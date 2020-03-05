export interface IFavLocation {
  id?: number;
  userFirstName?: string;
  userId?: number;
  destinationCity?: string;
  destinationId?: number;
}

export class FavLocation implements IFavLocation {
  constructor(
    public id?: number,
    public userFirstName?: string,
    public userId?: number,
    public destinationCity?: string,
    public destinationId?: number
  ) {}
}
