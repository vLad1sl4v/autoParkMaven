package by.incubator.application.service;

import by.incubator.application.entity.Rents;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.orm.EntityManager;

import java.util.List;

public class RentsService {
    @Autowired
    EntityManager entityManager;

    @InitMethod
    public void init() { }

    public Rents get(Long id) {
        return entityManager.get(id, Rents.class).get();
    }

    public List<Rents> getAll() {
        return entityManager.getAll(Rents.class);
    }

    public Long save(Rents rent) {
        return entityManager.save(rent);
    }
}
