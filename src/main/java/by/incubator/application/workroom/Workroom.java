package by.incubator.application.workroom;

import by.incubator.application.entity.Vehicles;
import by.incubator.application.fixer.Fixer;
import by.incubator.application.vehicle.Vehicle;
import by.incubator.application.infrastructure.core.annotations.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Workroom {
    @Autowired
    private Fixer mechanic;

    public Workroom() {}

    public void checkAllVehicles(List<Vehicles> vehicles) {
        List<Vehicles> notBrokenVehicles = new ArrayList<>();

        for (Vehicles vehicle : vehicles) {
            if (mechanic.isBroken(vehicle)) {
                System.out.println("Broken vehicle:\n" + vehicle);
            } else {
               notBrokenVehicles.add(vehicle);
            }
        }

        System.out.println("Not broken vehicles: ");

        for (Vehicles vehicle : notBrokenVehicles) {
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
