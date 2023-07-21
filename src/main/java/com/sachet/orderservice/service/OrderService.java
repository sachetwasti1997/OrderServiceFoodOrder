package com.sachet.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.orderservice.error.MenuNotFoundError;
import com.sachet.orderservice.error.OrderAlreadyPresent;
import com.sachet.orderservice.model.Menu;
import com.sachet.orderservice.model.Order;
import com.sachet.orderservice.model.OrderCreatedEventModel;
import com.sachet.orderservice.model.OrderStatus;
import com.sachet.orderservice.producer.OrderCreatedEventProducer;
import com.sachet.orderservice.repository.MenuRepository;
import com.sachet.orderservice.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderCreatedEventProducer producer;

    public OrderService(
            OrderRepository orderRepository,
            MenuRepository menuRepository,
            OrderCreatedEventProducer producer) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.producer = producer;
    }

    public Order createOrder(Order order) throws JsonProcessingException {
        var menuId = order.menuId();
        var menu = menuRepository.findMenuById(menuId);

        // Find if the menu really exists
        if (menu.isEmpty()) {
            throw new MenuNotFoundError("Menu with the id " + order.menuId() + " not found!"
                    , HttpStatus.NOT_FOUND);
        }

        //check if the item is not already reserved
        var orderStatus = orderRepository.findOrderByMenuIdAndStatusNot(menuId, OrderStatus.CANCELLED.name());
        if (orderStatus.isPresent()) {
            throw new OrderAlreadyPresent("Order already exists for the given menu", HttpStatus.BAD_REQUEST);
        }

        //set the expiration date and the status
        var expiration = new Date();
        expiration.setTime(expiration.getTime() + (5 * 1000 * 60));
        order.setExpiresAt(expiration);
        order.setStatus(OrderStatus.PENDING_PAYMENT.name());

        //save the order to the database
        var savedOrder = orderRepository.save(order);

        //publish order created event
        producer.sendOrderCreatedEvent(new OrderCreatedEventModel(
                savedOrder.id(),
                savedOrder.status(),
                savedOrder.userId(),
                savedOrder.expiresAt().toString(),
                savedOrder.menuId(),
                savedOrder.getMenuPrice()
        ));

        return savedOrder;
    }
}
