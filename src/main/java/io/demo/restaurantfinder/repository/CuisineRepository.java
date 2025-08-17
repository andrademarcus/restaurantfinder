package io.demo.restaurantfinder.repository;

import io.demo.restaurantfinder.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    Optional<Cuisine> findByNameIgnoreCase(String name);
}
