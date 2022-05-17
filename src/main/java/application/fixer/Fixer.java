package application.fixer;

import application.vehicle.Vehicle;

import java.util.Map;

public interface Fixer {
    Map<String, Integer> detectBreaking(Vehicle vehicle);

    void repair(Vehicle vehicle);

    default boolean detectAndRepair(Vehicle vehicle) {
        detectBreaking(vehicle);
        if (isBroken(vehicle)) {
            repair(vehicle);
            return true;
        }
        return false;
    }

    boolean isBroken(Vehicle vehicle);
}
