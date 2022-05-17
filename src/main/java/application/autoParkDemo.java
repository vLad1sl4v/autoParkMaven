package application;

import application.fixer.Fixer;
import application.fixer.impl.BadMechanicService;
import application.fixer.impl.MechanicService;
import application.vehicle.VehicleCollection;
import application.workroom.Workroom;
import application.infrastructure.core.impl.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class autoParkDemo {
    public static void main(String[] args) {
        Map<Class<?>, Class<?>> interfaceToImplementation =
                initInterfaceToImplementation(Fixer.class, MechanicService.class);
        ApplicationContext context = new ApplicationContext("application", interfaceToImplementation);
        VehicleCollection vehicleCollection = context.getObject(VehicleCollection.class);

        checkALlVehicles(context, vehicleCollection);

        interfaceToImplementation =
                initInterfaceToImplementation(Fixer.class, BadMechanicService.class);

        context = new ApplicationContext("application", interfaceToImplementation);
        vehicleCollection = context.getObject(VehicleCollection.class);

        checkALlVehicles(context, vehicleCollection);
    }

    private static void checkALlVehicles(ApplicationContext context, VehicleCollection vehicleCollection) {
        Workroom workroom = context.getObject(Workroom.class);
        workroom.checkAllVehicles(vehicleCollection.getVehicles());
    }

    private static Map<Class<?>, Class<?>> initInterfaceToImplementation(Class<?> iClazz, Class<?> clazz) {
        Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();
        interfaceToImplementation.put(iClazz, clazz);
        return interfaceToImplementation;
    }
}



