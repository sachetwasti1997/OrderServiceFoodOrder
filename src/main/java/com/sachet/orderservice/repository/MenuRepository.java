package com.sachet.orderservice.repository;

import com.sachet.orderservice.model.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, String> {
}
