package by.incubator.application;

import by.incubator.application.fixer.Fixer;
import by.incubator.application.fixer.impl.MechanicService;
import by.incubator.application.parser.IVehicleParser;
import by.incubator.application.parser.impl.ParserVehicleFromFile;
import by.incubator.application.vehicle.VehicleCollection;
import by.incubator.application.workroom.Workroom;
import by.incubator.application.infrastructure.core.impl.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class autoParkDemo {
    public static void main(String[] args) {
        Map<Class<?>, Class<?>> interfaceToImplementation = initInterfaceToImplementation();
        ApplicationContext context = new ApplicationContext("by.incubator.application", interfaceToImplementation);

        VehicleCollection vehicleCollection = context.getObject(VehicleCollection.class);
        Workroom workroom = context.getObject(Workroom.class);
        workroom.checkAllVehicles(vehicleCollection.getVehicles());
    }
    private static Map<Class<?>, Class<?>> initInterfaceToImplementation() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Fixer.class, MechanicService.class);
        map.put(IVehicleParser.class, ParserVehicleFromFile.class);
        return map;
    }
}



