package by.incubator.application.service;

import by.incubator.application.entity.Types;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.orm.EntityManager;

import java.util.List;

public class TypesService {
    @Autowired
    EntityManager entityManager;

    @InitMethod
    public void init() { }

    public Types get(Long id) {
        return entityManager.get(id, Types.class).get();
    }

    public List<Types> getAll() {
        return entityManager.getAll(Types.class);
    }

    public Long save(Types type) {
        return entityManager.save(type);
    }
}
