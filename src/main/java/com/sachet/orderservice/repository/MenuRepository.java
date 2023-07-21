package com.sachet.orderservice.repository;

import com.sachet.orderservice.model.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MenuRepository extends CrudRepository<Menu, String> {
    Optional<Menu> findMenuById(String id);
}
