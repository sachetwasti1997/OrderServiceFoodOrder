package com.sachet.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.orderservice.error.MenuNotFoundError;
import com.sachet.orderservice.error.OrderAlreadyPresent;
import com.sachet.orderservice.error.OrderNotFound;
import com.sachet.orderservice.model.*;
import com.sachet.orderservice.producer.OrderCancelledEventPublisher;
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

    private final OrderCancelledEventPublisher cancelledEventPublisher;

    public OrderService(
            OrderRepository orderRepository,
            MenuRepository menuRepository,
            OrderCreatedEventProducer producer,
            OrderCancelledEventPublisher cancelledEventPublisher) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.producer = producer;
        this.cancelledEventPublisher = cancelledEventPublisher;
    }

    public Order createOrder(Order order) throws JsonProcessingException {
        var menuId = order.getMenuId();
        var menu = menuRepository.findMenuById(menuId);

        // Find if the menu really exists
        if (menu.isEmpty()) {
            throw new MenuNotFoundError("Menu with the id " + order.getMenuId() + " not found!"
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
                savedOrder.getId(),
                savedOrder.getStatus(),
                savedOrder.getUserId(),
                savedOrder.getExpiresAt().toString(),
                savedOrder.getMenuId(),
                savedOrder.getMenuPrice()
        ));

        return savedOrder;
    }

    public String cancelOrder(Order order) throws JsonProcessingException {
        var orderDB = orderRepository.findOrderByIdAndStatusNot(order.getId(), OrderStatus.CANCELLED.name());

        // Find if the menu really exists
        if (orderDB.isEmpty()) {
            throw new OrderNotFound("Order with the id " + order.getId() + " not found!" +
                    " Or it is already cancelled!"
                    , HttpStatus.NOT_FOUND);
        }

        order = orderDB.get();
        order.setStatus(OrderStatus.CANCELLED.name());

        orderRepository.save(order);

        cancelledEventPublisher.sendOrderCancelEvent(
                new OrderCancelledEvent(order.getId(), order.getMenuId())
        );

        return "Cancelled Order "+order.getId();
    }
}
