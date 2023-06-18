import { CityLocation } from "./location";
import { User } from "./user";

export class Trip {
    id?: string;
    startingLocation?: CityLocation;
    finishingLocation?: CityLocation;
    startingTime?: string;
    finishingTime?: string;
    persons?: string;
    status?: string;
    user?: User;
    selected?: boolean;
}