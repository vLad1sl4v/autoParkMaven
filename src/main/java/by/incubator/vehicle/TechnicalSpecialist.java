package by.incubator.vehicle;

import by.incubator.engines.DieselEngine;
import by.incubator.engines.ElectricalEngine;
import by.incubator.engines.GasolineEngine;

public class TechnicalSpecialist {
    public static final int LOWER_LIMIT_MANUFACTURE_YEAR = 1886;

    static public boolean validateManufactureYear(int year) {
        return year >= LOWER_LIMIT_MANUFACTURE_YEAR && year <= 9999;
    }

    static public boolean validateMileage(int mileage) {
        return mileage >= 0;
    }

    static public boolean validateWeight(int weight) {
        return weight >= 0;
    }

    static public boolean validateColor (Colors color) {
        return color != null;
    }

    static public boolean validateVehicleType (VehicleType type) {
        return type.getTypeName() != null && !type.getTypeName().equals("") && type.getTaxCoefficient() > 0;
    }

    static public boolean validateRegistrationNumber (String number) {
        if (number != null) {

            if (number.length() != 9) return false;

            char[] charStr = number.toCharArray();

            for (int i = 0; i < 4; i++) {
                if (charStr[i] < '0' || charStr[i] > '9') return false;
            }

            if (charStr[4] != ' ') return false;

            for (int i = 5; i <= 6; i++) {
                if (charStr[i] < 'A' || charStr[i] > 'Z') return false;
            }

            if (charStr[7] != '-') return false;

            if (charStr[8] < '0' || charStr[8] > '9') return false;
        }

        return true;
    }

    public static boolean validateModelName(String name) {
        return name != null && !name.equals("");
    }

    public static boolean validateGasolineEngine (GasolineEngine gasolineEngine) {
        return gasolineEngine.getEngineCapacity() > 0 &&
                gasolineEngine.getFuelConsumptionPer100() > 0 &&
                gasolineEngine.getFuelTankCapacity() > 0;
    }

    public static boolean validateElectricalEngine (ElectricalEngine electricalEngine) {
        return electricalEngine.getBatterySize() > 0 &&
                electricalEngine.getElectricityConsumption() > 0;
    }

    public static boolean validateDieselEngine (DieselEngine dieselEngine) {
        return dieselEngine.getEngineCapacity() > 0 &&
                dieselEngine.getFuelConsumptionPer100() > 0 &&
                dieselEngine.getFuelTankCapacity() > 0;
    }
}
