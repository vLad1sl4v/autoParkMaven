package by.incubator.application.parser;

import by.incubator.application.vehicle.Vehicle;
import by.incubator.application.infrastructure.core.annotations.InitMethod;

import java.io.*;

public class ParserBreakingsFromFile {
    private final static String ORDERS_PATH = "src/main/resources/data/orders.csv";
    private final static String ORDERS_TEMP_PATH = "src/main/resources/data/ordersTemp.csv";

    public ParserBreakingsFromFile() {}

    @InitMethod
    public void init(){}

    public boolean findBrokenVehicle(Vehicle vehicle) {
        String partOfLine = vehicle.getId() + ":";
        String currentLine;

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_PATH))){
            while ((currentLine = reader.readLine()) != null) {
                String idPart = getIdPart(currentLine);
                if (idPart.equals(partOfLine)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File reading exception");
        }

        return false;
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
}
