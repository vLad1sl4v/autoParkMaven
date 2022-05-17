package application.workroom;

import application.fixer.Fixer;
import application.vehicle.Vehicle;
import application.infrastructure.core.annotations.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Workroom {
    @Autowired
    private Fixer mechanic;

    public Workroom() {}

    public void checkAllVehicles(List<Vehicle> vehicles) {
        List<Vehicle> notBrokenVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            if (mechanic.isBroken(vehicle)) {
                System.out.println("Broken vehicle:\n" + vehicle);
            } else {
               notBrokenVehicles.add(vehicle);
            }
        }

        System.out.println("Not broken vehicles: ");

        for (Vehicle vehicle : notBrokenVehicles) {
            System.out.println(vehicle);
        }
    }

    public Fixer getMechanic() {
        return mechanic;
    }

    public void setMechanic(Fixer mechanic) {
        this.mechanic = mechanic;
    }
}
