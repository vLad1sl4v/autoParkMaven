package by.incubator.application.service;

import by.incubator.application.entity.Orders;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.orm.EntityManager;

import java.util.List;

public class OrdersService {
    @Autowired
    EntityManager entityManager;

    @InitMethod
    public void init() { }

    public Orders get(Long id) {
        return entityManager.get(id, Orders.class).get();
    }

    public List<Orders> getAll() {
        return entityManager.getAll(Orders.class);
    }

    public Long save(Orders order) {
        return entityManager.save(order);
    }
}
