import { CityLocation } from "./location"

export class Road {
    id?: string;
    startingLocation?: CityLocation;
    finishingLocation?: CityLocation;
    cost?: string;
    selected?: boolean;
}