import { Vehicle } from "./vehicle";

export class CityLocation {
    id?: string;
    name?: string;
    type?: string;
    vehicles?: Vehicle[];
    selected?: boolean;
}