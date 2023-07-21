package com.sachet.orderservice.repository;

import com.sachet.orderservice.model.Order;
import com.sachet.orderservice.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, String> {
    Optional<Order> findOrderById(String id);
    Optional<Order> findOrderByIdAndStatusNot(String id, String status);
    Optional<Order> findOrderByMenuIdAndStatusNot(String menuId, String status);
}
