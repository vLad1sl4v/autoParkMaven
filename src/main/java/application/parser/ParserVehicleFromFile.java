package application.parser;

import application.engines.DieselEngine;
import application.engines.ElectricalEngine;
import application.engines.GasolineEngine;
import application.engines.Startable;
import application.vehicle.*;
import application.infrastructure.core.annotations.Autowired;
import application.infrastructure.core.annotations.InitMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParserVehicleFromFile {
    private String rentsPath = "src/main/resources/data/rents.csv";
    private String typesPath = "src/main/resources/data/types.csv";
    private String vehiclesPath = "src/main/resources/data/vehicles.csv";

    @Autowired
    private TechnicalSpecialist specialist;

    public ParserVehicleFromFile() {
    }

    @InitMethod
    public void init() {

    }

    public List<VehicleType> loadTypes() {
        String filePath = typesPath;
        String contentLine;
        List<String> vehicleTypeInfo;
        List<VehicleType> types = new ArrayList<>();

        File typesFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(typesFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                vehicleTypeInfo = splitContentLine(contentLine);
                VehicleType vehicleType = createType(vehicleTypeInfo);
                types.add(vehicleType);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return types;
    }

    public VehicleType createType(String csvString) {
        return createType(splitContentLine(csvString));
    }

    public List<Rent> loadRents() {
        String filePath = rentsPath;
        String contentLine;
        List<String> rentsInfo;
        List<Rent> rents = new ArrayList<>();

        File rentsFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(rentsFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                rentsInfo = splitContentLine(contentLine);
                Rent rent = createRent(rentsInfo);
                rents.add(rent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rents;
    }

    public List<Vehicle> loadVehicles() {
        String filePath = vehiclesPath;
        String contentLine;
        List<String> vehiclesInfo;
        List<Vehicle> vehicles = new ArrayList<>();

        File vehiclesFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(vehiclesFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                vehiclesInfo = splitContentLine(contentLine);
                Vehicle vehicle = createVehicle(vehiclesInfo);
                vehicles.add(vehicle);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public Vehicle createVehicle(String csvString) {
        return createVehicle(splitContentLine(csvString));
    }

    public double sumTotalProfit() {
        double sum = 0.0;

        for (Vehicle vehicle : loadVehicles()) {
            sum += vehicle.getTotalProfit();
        }

        return sum;
    }

    public void display() {
        System.out.printf("%5s %10s %20s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Id", "Type", "ModelName",
                "Number", "Weight", "Year", "MileAge", "Color", "Income", "Tax", "Profit");

        for (Vehicle vehicle : loadVehicles()) {
            System.out.printf("%5s %10s %20s %10s %10s %10s %10s %10s %10.2f %10.2f %10.2f\n", vehicle.getId(), vehicle.getVehicleType().getTypeName(),
                    vehicle.getModelName(), vehicle.getRegistrationNumber(), vehicle.getMass(), vehicle.getManufactureYear(),
                    vehicle.getMileAge(), vehicle.getColor(), vehicle.getTotalIncome(), vehicle.getCalcTaxPerMonth(),
                    vehicle.getTotalProfit());
        }

        System.out.printf("%5s %119.2f\n", "Total", sumTotalProfit());
    }

    private VehicleType createType(List<String> vehicleTypeInfo) {
        final int idNum = 0;
        final int typeNameNum = 1;
        final int taxCoefficientNum = 2;

        int id = Integer.parseInt(vehicleTypeInfo.get(idNum));
        String typeName = vehicleTypeInfo.get(typeNameNum);

        String taxCoefficientStr = vehicleTypeInfo.get(taxCoefficientNum);
        double taxCoefficient = Double.parseDouble(taxCoefficientStr);

        return new VehicleType(id, typeName, taxCoefficient);
    }

    private List<String> splitContentLine(String contentLine) {
        List<String> info = new ArrayList<>();

        int quoteCounter = 0;

        char[] charContentLine = contentLine.toCharArray();
        StringBuilder add = new StringBuilder();

        for (char c : charContentLine) {
            if (c == '"') {
                quoteCounter++;
            }
            if (!isSeparator(c, quoteCounter)) {
                if (c == ',') {
                    add.append('.');
                } else if (c != '\"') {
                    add.append(c);
                }
            } else {
                info.add(add.toString());
                add = new StringBuilder();
            }
        }

        info.add(add.toString());


        return info;
    }

    private boolean isSeparator(char ch, int quoteCounter) {
        final char separator = ',';

        return (quoteCounter % 2 == 0)
                && ch == separator;
    }


    private Rent createRent(List<String> rentsInfo) {
        final int idNum = 0;
        final int dateNum = 1;
        final int costNum = 2;

        int id = Integer.parseInt(rentsInfo.get(idNum));

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date;

        try {
            date = format.parse(rentsInfo.get(dateNum));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("incorrect date format", e);
        }

        String costStr = rentsInfo.get(costNum);
        double cost = Double.parseDouble(costStr);

        getVehicle(id).getRents().add(new Rent(id, date, cost));

        return new Rent(id, date, cost);
    }

    private Vehicle createVehicle(List<String> vehiclesInfo) {
        final int idNum = 0;
        final int typeNum = 1;
        final int modelNameNum = 2;
        final int registrationNumberNum = 3;
        final int massNum = 4;
        final int manufactureYearNum = 5;
        final int mileAgeNum = 6;
        final int colorNum = 7;
        final int engineTypeNum = 8;

        int id = Integer.parseInt(vehiclesInfo.get(idNum));

        int typeId = Integer.parseInt(vehiclesInfo.get(typeNum));
        VehicleType vehicleType = getVehicleType(typeId);

        String modelName = vehiclesInfo.get(modelNameNum);
        String registrationNumber = vehiclesInfo.get(registrationNumberNum);
        int mass = Integer.parseInt(vehiclesInfo.get(massNum));
        int manufactureYear = Integer.parseInt(vehiclesInfo.get(manufactureYearNum));
        int mileAge = Integer.parseInt(vehiclesInfo.get(mileAgeNum));
        Colors color = Colors.valueOf(vehiclesInfo.get(colorNum).toUpperCase(Locale.ROOT));

        String engineName = vehiclesInfo.get(engineTypeNum);
        Startable engine = createEngine(engineName, vehiclesInfo);

        return new Vehicle(id, vehicleType, modelName, registrationNumber, mass, manufactureYear, mileAge, color, engine);
    }

    private Startable createEngine(String engineName, List<String> vehiclesInfo) {
        final int engineCapacityNum = 9;
        final int fuelTankCapacityNum = 10;
        final int fuelConsumptionNum = 11;
        final int batterySizeNum = 9;
        final int electricityConsumptionNum = 10;

        double engineCapacity;
        double fuelTankCapacity;
        double fuelConsumptionPer100;
        double batterySize;
        double electricityConsumption;

        if (engineName.equalsIgnoreCase("Diesel") || engineName.equalsIgnoreCase("Gasoline")) {
            String engineCapacityStr = vehiclesInfo.get(engineCapacityNum);
            engineCapacity = Double.parseDouble(engineCapacityStr);

            String fuelTankCapacityStr = vehiclesInfo.get(fuelTankCapacityNum);
            fuelTankCapacity = Double.parseDouble(fuelTankCapacityStr);

            String fuelConsumptionStr = vehiclesInfo.get(fuelConsumptionNum);
            fuelConsumptionPer100 = Double.parseDouble(fuelConsumptionStr);

            if (engineName.equalsIgnoreCase("Diesel")) {
                return new DieselEngine(engineCapacity, fuelTankCapacity, fuelConsumptionPer100);
            } else {
                return new GasolineEngine(engineCapacity, fuelTankCapacity, fuelConsumptionPer100);
            }
        } else if (engineName.equalsIgnoreCase("Electrical")) {
            String batterySizeStr = vehiclesInfo.get(batterySizeNum);
            batterySize = Double.parseDouble(batterySizeStr);

            String electricityConsumptionStr = vehiclesInfo.get(electricityConsumptionNum);
            electricityConsumption = Double.parseDouble(electricityConsumptionStr);

            return new ElectricalEngine(batterySize, electricityConsumption);
        } else {
            throw new IllegalArgumentException("Incorrect engine type");
        }
    }

    private Vehicle getVehicle(int vehicleId) {

        for (Vehicle vehicle : loadVehicles()) {
            if (vehicleId == vehicle.getId()) {
                return vehicle;
            }
        }
        throw new IllegalArgumentException();
    }

    private VehicleType getVehicleType(int typeId) {
        for (VehicleType vehicleType : loadTypes()) {
            if (typeId == vehicleType.getId()) {
                return vehicleType;
            }
        }
        throw new IllegalArgumentException();
    }
}