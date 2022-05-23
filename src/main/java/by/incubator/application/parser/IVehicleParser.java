package by.incubator.application.parser;

import by.incubator.application.entity.*;

import java.util.List;

public interface IVehicleParser {
    List<Types> loadTypes();
    List<Vehicles> loadVehicles();
    List<Rents> loadRents();
}
