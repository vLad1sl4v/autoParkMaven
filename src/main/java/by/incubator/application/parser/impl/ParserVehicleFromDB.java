package by.incubator.application.parser.impl;

import by.incubator.application.entity.Rents;
import by.incubator.application.entity.Types;
import by.incubator.application.entity.Vehicles;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.parser.IVehicleParser;
import by.incubator.application.service.RentsService;
import by.incubator.application.service.TypesService;
import by.incubator.application.service.VehiclesService;

import java.util.List;

public class ParserVehicleFromDB implements IVehicleParser {
    @Autowired
    private TypesService typesService;
    @Autowired
    private VehiclesService vehiclesService;
    @Autowired
    private RentsService rentsService;

    @Override
    public List<Types> loadTypes() {
        return typesService.getAll();
    }

    @Override
    public List<Vehicles> loadVehicles() {
        return vehiclesService.getAll();
    }

    @Override
    public List<Rents> loadRents() {
        return rentsService.getAll();
    }
}
