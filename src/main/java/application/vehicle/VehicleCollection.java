package application.vehicle;

import application.parser.ParserVehicleFromFile;
import application.infrastructure.core.annotations.Autowired;
import application.infrastructure.core.annotations.InitMethod;

import java.util.*;

public class VehicleCollection {
    private List<VehicleType> vehicleTypes = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    @Autowired
    private ParserVehicleFromFile parser;

    @InitMethod
    public void init() {
        vehicles = parser.loadVehicles();
        vehicleTypes = parser.loadTypes();
    }

    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ParserVehicleFromFile getParser() {
        return parser;
    }

    public void setParser(ParserVehicleFromFile parser) {
        this.parser = parser;
    }
}
