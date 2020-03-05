import { UserType } from 'app/shared/model/enumerations/user-type.model';

export interface ICarPoolingUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  password?: string;
  type?: UserType;
}

export class CarPoolingUser implements ICarPoolingUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public password?: string,
    public type?: UserType
  ) {}
}
