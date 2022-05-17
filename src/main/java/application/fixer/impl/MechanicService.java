package application.fixer.impl;

import application.fixer.Fixer;
import application.parser.ParserBreakingsFromFile;
import application.vehicle.Vehicle;
import application.infrastructure.core.annotations.Autowired;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MechanicService implements Fixer {
    private static final String[] details = {"Фильтр", "Втулка", "Вал", "Ось", "Свеча",
            "Масло", "ГРМ", "ШРУС"};
    private final static String ORDERS_PATH = "src/main/resources/data/orders.csv";
    private final static String ORDERS_TEMP_PATH = "src/main/resources/data/ordersTemp.csv";
    @Autowired
    private ParserBreakingsFromFile parser;

    public MechanicService() {}

    public ParserBreakingsFromFile getParser() {
        return parser;
    }

    public void setParser(ParserBreakingsFromFile parser) {
        this.parser = parser;
    }

    @Override
    public Map<String, Integer> detectBreaking(Vehicle vehicle) {
        Map<String, Integer> breakage = new HashMap<>();
        int detailsTypeNum = details.length;
        Random random = new Random();

        int detailNum = random.nextInt(detailsTypeNum + 1);
        int breakageNum = random.nextInt(4) + 1;

        if (detailNum == detailsTypeNum) {
            breakage.put(null, 0);
        }
        else {
            String brokenDetail = details[detailNum];
            breakage.put(brokenDetail, breakageNum);

            try {
                writeBreakageInFile(brokenDetail, breakageNum, vehicle, ORDERS_PATH);
                writeBreakageInFile(brokenDetail, breakageNum, vehicle, ORDERS_TEMP_PATH);
            } catch (IOException e) {
                System.out.println("Incorrect file path, file not found");
            }
        }

        return breakage;
    }

    @Override
    public void repair(Vehicle vehicle) {
        String partOfLine = vehicle.getId() + ":";

        deleteBrokenVehicleLine(partOfLine, ORDERS_TEMP_PATH, ORDERS_PATH);
        deleteBrokenVehicleLine(partOfLine, ORDERS_PATH, ORDERS_TEMP_PATH);
    }

    @Override
    public boolean isBroken(Vehicle vehicle) {
        return parser.findBrokenVehicle(vehicle);
    }

    private void deleteBrokenVehicleLine(String partOfLine, String ordersPathTemp, String ordersPath) {
        String currentLine;

        try (BufferedReader reader = new BufferedReader(new FileReader(ordersPathTemp));
             BufferedWriter writer = new BufferedWriter(new FileWriter(ordersPath))){
            while ((currentLine = reader.readLine()) != null) {
                String idPart = getIdPart(currentLine);
                if (idPart.equals(partOfLine)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            System.out.println("File reading exception");
        }
    }

    private String getIdPart(String currentLine) {
        char[] charLine = currentLine.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : charLine) {
            if (c != ':') {
                stringBuilder.append(c);
            } else {
                stringBuilder.append(c);
                break;
            }
        }

        return stringBuilder.toString();
    }

    private void writeBreakageInFile(String brokenDetail, int breakageNum, Vehicle vehicle, String path) throws IOException {
        File ordersFile = new File(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile, true));
        writer.write(vehicle.getId() + ": " + brokenDetail + "," + breakageNum);
        writer.newLine();
        writer.close();
    }

}
