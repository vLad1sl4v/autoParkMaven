package by.incubator.application;

import by.incubator.application.fixer.Fixer;
import by.incubator.application.fixer.impl.MechanicService;
import by.incubator.application.parser.IVehicleParser;
import by.incubator.application.parser.impl.ParserVehicleFromFile;
import by.incubator.application.vehicle.VehiclesChecker;

import by.incubator.application.infrastructure.core.impl.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class autoParkDemo {
    public static void main(String[] args) {
        Map<Class<?>, Class<?>> interfaceToImplementation = initInterfaceToImplementation();
        ApplicationContext context = new ApplicationContext("by.incubator.application", interfaceToImplementation);

        VehiclesChecker vehicleChecker = context.getObject(VehiclesChecker.class);
        vehicleChecker.moveVehiclesToWorkroom(context);
        sleepMain();
    }

    private static void sleepMain() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Map<Class<?>, Class<?>> initInterfaceToImplementation() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Fixer.class, MechanicService.class);
        map.put(IVehicleParser.class, ParserVehicleFromFile.class);
        return map;
    }
}



