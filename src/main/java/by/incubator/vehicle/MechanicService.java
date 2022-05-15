package by.incubator.vehicle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MechanicService implements Fixer{
    private static final String[] details = {"Фильтр", "Втулка", "Вал", "Ось", "Свеча",
            "Масло", "ГРМ", "ШРУС"};
    private final static String ORDERS_PATH = "src/by/incubator/data/orders.csv";
    private final static String ORDERS_PATH_TEMP = "src/by/incubator/data/ordersTemp.csv";


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
                writeBreakageInFile(brokenDetail, breakageNum, vehicle, ORDERS_PATH_TEMP);
            } catch (IOException e) {
                System.out.println("Incorrect file path, file not found");
            }
        }

        return breakage;
    }

    private void writeBreakageInFile(String brokenDetail, int breakageNum, Vehicle vehicle, String path) throws IOException {
        File ordersFile = new File(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile, true));
        writer.write(vehicle.getId() + ": " + brokenDetail + "," + breakageNum);
        writer.newLine();
        writer.close();
    }

    @Override
    public void repair(Vehicle vehicle) {
        String partOfLine = vehicle.getId() + ":";

        deleteBrokenVehicleLine(partOfLine, ORDERS_PATH_TEMP, ORDERS_PATH);
        deleteBrokenVehicleLine(partOfLine, ORDERS_PATH, ORDERS_PATH_TEMP);
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

        for (int i = 0; i < charLine.length; i++) {
            if (charLine[i] != ':') {
                stringBuilder.append(charLine[i]);
            } else {
                stringBuilder.append(charLine[i]);
                break;
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean isBroken(Vehicle vehicle) {
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
}
