package by.incubator.application.fixer;

import by.incubator.application.entity.Vehicles;
import by.incubator.application.vehicle.Vehicle;

import java.util.Map;

public interface Fixer {
    Map<String, Integer> detectBreaking(Vehicles vehicle);

    void repair(Vehicles vehicle);

    default boolean detectAndRepair(Vehicles vehicle) {
        detectBreaking(vehicle);
        if (isBroken(vehicle)) {
            repair(vehicle);
            return true;
        }
        return false;
    }

    boolean isBroken(Vehicles vehicle);
}
