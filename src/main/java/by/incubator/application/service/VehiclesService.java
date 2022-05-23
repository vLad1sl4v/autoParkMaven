package by.incubator.application.service;

import by.incubator.application.entity.Orders;
import by.incubator.application.entity.Vehicles;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.orm.EntityManager;

import java.util.List;

public class VehiclesService {
    @Autowired
    EntityManager entityManager;

    @InitMethod
    public void init() { }

    public Vehicles get(Long id) {
        return entityManager.get(id, Vehicles.class).get();
    }

    public List<Vehicles> getAll() {
        return entityManager.getAll(Vehicles.class);
    }

    public Long save(Vehicles vehicle) {
        return entityManager.save(vehicle);
    }
}
