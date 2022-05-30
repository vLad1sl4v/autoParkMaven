package by.incubator.application.parser;

import by.incubator.application.entity.Vehicles;
import by.incubator.application.vehicle.Vehicle;

public interface IBreakingsParser {
    boolean findBrokenVehicle(Vehicles vehicle);
}
