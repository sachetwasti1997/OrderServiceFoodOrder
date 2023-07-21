package com.sachet.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.orderservice.model.Menu;
import com.sachet.orderservice.model.Order;
import com.sachet.orderservice.repository.MenuRepository;
import com.sachet.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class TestController {
    private final OrderService orderService;
    private final MenuRepository menuRepository;

    public TestController(OrderService orderService
    , MenuRepository menuRepository) {
        this.orderService = orderService;
        this.menuRepository = menuRepository;
    }
    @PostMapping("/create")
    private ResponseEntity<Order> createOrder(@RequestBody Order order) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.createOrder(order));
    }
    @GetMapping("/menu")
    private ResponseEntity<Iterable<Menu>> getAllMenu() {
        return ResponseEntity.ok(menuRepository.findAll());
    }
}
