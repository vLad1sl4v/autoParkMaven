package by.incubator.application.vehicle;

import by.incubator.application.engines.Startable;
import by.incubator.application.Exceptions.NotVehicleException;

import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Comparable<Vehicle> {
    private VehicleType vehicleType;
    private String modelName;
    private String registrationNumber;
    private int mass;
    private int manufactureYear;
    private int mileAge;
    private Colors color;
    private double tankLitres;
    private int id;
    List<Rent> rents = new ArrayList<>();
    Startable engine;

    public Vehicle(int id, VehicleType vehicleType, String modelName, String registrationNumber, int mass, int manufactureYear,
                   int mileAge, Colors color, Startable engine) {
        try {
            if (!TechnicalSpecialist.validateVehicleType(vehicleType)) {
                throw new NotVehicleException("Vehicle Type: " + vehicleType);
            } else {
                this.vehicleType = vehicleType;
            }

            if (!TechnicalSpecialist.validateModelName(modelName)) {
                throw new NotVehicleException("Model name: " + modelName);
            } else {
                this.modelName = modelName;
            }

            if (!TechnicalSpecialist.validateRegistrationNumber(registrationNumber)) {
                throw new NotVehicleException("Registration number: " + registrationNumber);
            } else {
                this.registrationNumber = registrationNumber;
            }

            if (!TechnicalSpecialist.validateWeight(mass)) {
                throw new NotVehicleException("Mass " + mass);
            } else {
                this.mass = mass;
            }

            if (!TechnicalSpecialist.validateManufactureYear(manufactureYear)) {
                throw new NotVehicleException("Manufacture year: " + manufactureYear);
            } {
                this.manufactureYear = manufactureYear;
            }

            if (!TechnicalSpecialist.validateMileage(mileAge)) {
                throw new NotVehicleException("Mile age: " + mileAge);
            } else {
                this.mileAge = mileAge;
            }

            if (!TechnicalSpecialist.validateColor(color)) {
                throw new NotVehicleException("Color: " + color);
            } else {
                this.color = color;
            }

            this.id = id;

            this.engine = engine;
        } catch (NotVehicleException e) {
            System.out.println("Vehicle is not created.");
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }

    public VehicleType getVehicleType () {
        return vehicleType;
    }

    public String getModelName() {
        return modelName;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Startable getEngine() {
        return engine;
    }

    public void setEngine(Startable engine) {
        this.engine = engine;
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (TechnicalSpecialist.validateRegistrationNumber(registrationNumber)) {
            this.registrationNumber = registrationNumber;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        if (TechnicalSpecialist.validateWeight(mass)) {
            this.mass = mass;
        }
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public int getMileAge() {
        return mileAge;
    }

    public void setMileAge(int mileAge) {
        if (TechnicalSpecialist.validateMileage(mileAge)) {
            this.mileAge = mileAge;
        }
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public double getTankLitres() {
        return tankLitres;
    }

    public void setTankLitres(double tankLitres) {
        if (tankLitres > 0) {
            this.tankLitres = tankLitres;
        }
    }

    public double getCalcTaxPerMonth() {
        double scale = Math.pow(10, 2);
        double result = mass * 0.0013 + engine.getTaxPerMonth() * vehicleType.getTaxCoefficient() * 30 + 5;
        result = Math.ceil(result * scale) / scale;

        return result;
    }

    public double getTotalIncome() {
        double result = 0.0;

        for (Rent rent : rents) {
            result += rent.getRentCost();
        }

        return Math.ceil(result * 100) / 100;
    }

    public double getTotalProfit() {
        return Math.ceil((getTotalIncome() - getCalcTaxPerMonth()) * 100) / 100;
    }

    @Override
    public String toString() {
        return vehicleType.getTypeName() + "," + modelName + "," + registrationNumber + "," + mass + ","
                + manufactureYear + "," + mileAge + "," + color + "," + "\"" + getCalcTaxPerMonth() + "\"" + "," + engine.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleType.equals(vehicle.vehicleType)
                && modelName.equals(vehicle.modelName);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + vehicleType.hashCode();
        hash = 31 * hash + modelName.hashCode();

        return hash;
    }

    @Override
    public int compareTo(Vehicle o) {
        int result = 0;

        if (manufactureYear < o.manufactureYear) {result = -1;}
        else if (manufactureYear > o.manufactureYear) {result = 1;}
        else {
            if (mileAge < o.mileAge) {result = -1;}
            if (mileAge > o.mileAge) {result = 1;}
        }

        return result;
    }
}
