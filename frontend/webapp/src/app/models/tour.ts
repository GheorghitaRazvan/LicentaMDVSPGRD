import { CityLocation } from "./location";
import { Trip } from "./trip";
import { Vehicle } from "./vehicle";

export class Tour {
    depot?: CityLocation;
    trips?: Trip[];
    vehicle?: Vehicle;
}