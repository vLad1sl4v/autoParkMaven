package by.incubator.application.vehicle;

import by.incubator.application.entity.Rents;
import by.incubator.application.entity.Types;
import by.incubator.application.entity.Vehicles;
import by.incubator.application.parser.impl.ParserVehicleFromFile;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;

import java.util.*;

public class VehicleCollection {
    private List<Types> vehicleTypes;
    private List<Vehicles> vehicles;
    private List<Rents> rents;

    @Autowired
    private ParserVehicleFromFile parser;

    @InitMethod
    public void init() {
        vehicles = parser.loadVehicles();
        vehicleTypes = parser.loadTypes();
        rents = parser.loadRents();
    }

    public List<Types> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<Types> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public List<Vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Rents> getRents() {
        return rents;
    }

    public ParserVehicleFromFile getParser() {
        return parser;
    }

    public void setParser(ParserVehicleFromFile parser) {
        this.parser = parser;
    }
}
