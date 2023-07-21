package com.sachet.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.orderservice.model.Menu;
import com.sachet.orderservice.model.Order;
import com.sachet.orderservice.repository.MenuRepository;
import com.sachet.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final MenuRepository menuRepository;

    public OrderController(OrderService orderService
    , MenuRepository menuRepository) {
        this.orderService = orderService;
        this.menuRepository = menuRepository;
    }
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.createOrder(order));
    }
    @PutMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody Order order) throws JsonProcessingException{
        return ResponseEntity.ok(orderService.cancelOrder(order));
    }
    @GetMapping("/menu")
    public ResponseEntity<Iterable<Menu>> getAllMenu() {
        return ResponseEntity.ok(menuRepository.findAll());
    }
}
