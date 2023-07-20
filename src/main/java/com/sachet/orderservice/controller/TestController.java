package com.sachet.orderservice.controller;

import com.sachet.orderservice.model.Menu;
import com.sachet.orderservice.repository.MenuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class TestController {
    private final MenuRepository menuRepository;
    public TestController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    @GetMapping("/menu")
    private ResponseEntity<Iterable<Menu>> getAllMenu() {
        return ResponseEntity.ok(menuRepository.findAll());
    }
}
