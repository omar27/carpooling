export interface ILocation {
  id?: number;
  city?: string;
  area?: string;
  xAxis?: number;
  yAxis?: number;
}

export class Location implements ILocation {
  constructor(public id?: number, public city?: string, public area?: string, public xAxis?: number, public yAxis?: number) {}
}
