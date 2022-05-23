package by.incubator.application.fixer.impl;

import by.incubator.application.entity.Vehicles;
import by.incubator.application.fixer.Fixer;
import by.incubator.application.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class BadMechanicService implements Fixer {

    @Override
    public Map<String, Integer> detectBreaking(Vehicles vehicle) {
        return new HashMap<>();
    }

    @Override
    public void repair(Vehicles vehicle) {

    }

    @Override
    public boolean isBroken(Vehicles vehicle) {
        return false;
    }
}
