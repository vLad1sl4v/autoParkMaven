package by.incubator.application.parser.impl;

import by.incubator.application.engines.DieselEngine;
import by.incubator.application.engines.ElectricalEngine;
import by.incubator.application.engines.GasolineEngine;
import by.incubator.application.engines.Startable;
import by.incubator.application.entity.Rents;
import by.incubator.application.entity.Types;
import by.incubator.application.entity.Vehicles;
import by.incubator.application.parser.IVehicleParser;
import by.incubator.application.vehicle.*;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParserVehicleFromFile implements IVehicleParser {
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

    public List<Types> loadTypes() {
        String filePath = typesPath;
        String contentLine;
        List<String> vehicleTypeInfo;
        List<Types> types = new ArrayList<>();

        File typesFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(typesFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                vehicleTypeInfo = splitContentLine(contentLine);
                Types type = createType(vehicleTypeInfo);
                types.add(type);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return types;
    }

    public Types createType(String csvString) {
        return createType(splitContentLine(csvString));
    }

    public List<Rents> loadRents() {
        String filePath = rentsPath;
        String contentLine;
        List<String> rentsInfo;
        List<Rents> rents = new ArrayList<>();

        File rentsFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(rentsFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                rentsInfo = splitContentLine(contentLine);
                Rents rent = createRent(rentsInfo);
                rents.add(rent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rents;
    }

    public List<Vehicles> loadVehicles() {
        String filePath = vehiclesPath;
        String contentLine;
        List<String> vehiclesInfo;
        List<Vehicles> vehicles = new ArrayList<>();

        File vehiclesFile = new File(filePath);

        try {
            Scanner scanner = new Scanner(vehiclesFile);
            while (scanner.hasNext()) {
                contentLine = scanner.nextLine();
                vehiclesInfo = splitContentLine(contentLine);
                Vehicles vehicle = createVehicle(vehiclesInfo);
                vehicles.add(vehicle);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public Vehicles createVehicle(String csvString) {
        return createVehicle(splitContentLine(csvString));
    }

    private Types createType(List<String> vehicleTypeInfo) {
        final int idNum = 0;
        final int typeNameNum = 1;
        final int taxCoefficientNum = 2;

        int id = Integer.parseInt(vehicleTypeInfo.get(idNum));
        String typeName = vehicleTypeInfo.get(typeNameNum);

        String taxCoefficientStr = vehicleTypeInfo.get(taxCoefficientNum);
        double taxCoefficient = Double.parseDouble(taxCoefficientStr);

        return new Types((long) id, typeName, taxCoefficient);
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


    private Rents createRent(List<String> rentsInfo) {
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

        return new Rents((long)id, date, cost);
    }

    private Vehicles createVehicle(List<String> vehiclesInfo) {
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
        Types vehicleType = getVehicleType(typeId);

        String modelName = vehiclesInfo.get(modelNameNum);
        String registrationNumber = vehiclesInfo.get(registrationNumberNum);
        int mass = Integer.parseInt(vehiclesInfo.get(massNum));
        int manufactureYear = Integer.parseInt(vehiclesInfo.get(manufactureYearNum));
        int mileAge = Integer.parseInt(vehiclesInfo.get(mileAgeNum));
        Colors color = Colors.valueOf(vehiclesInfo.get(colorNum).toUpperCase(Locale.ROOT));

        String engineName = vehiclesInfo.get(engineTypeNum);
        Startable engine = createEngine(engineName, vehiclesInfo);

        return new Vehicles((long)id, (long)vehicleType.getId(), modelName, registrationNumber, mass, manufactureYear, mileAge, color.toString(), engine.toString());
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

    private Vehicles getVehicle(int vehicleId) {
        for (Vehicles vehicle : loadVehicles()) {
            if (vehicleId == vehicle.getId()) {
                return vehicle;
            }
        }
        throw new IllegalArgumentException();
    }

    private Types getVehicleType(int typeId) {
        for (Types type : loadTypes()) {
            if (typeId == type.getId()) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }
}