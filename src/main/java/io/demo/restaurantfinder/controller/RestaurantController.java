package io.demo.restaurantfinder.controller;

import io.demo.restaurantfinder.model.RestaurantSearchResultDto;
import io.demo.restaurantfinder.service.RestaurantService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantSearchResultDto>> searchRestaurants(
            @RequestParam("name") Optional<String> name,
            @RequestParam("cuisine") Optional<String> cuisine,
            @RequestParam("minCustomerRating") Optional<@Min(1) @Max(5) Integer> minCustomerRating,
            @RequestParam("maxDistance") Optional<@Min(0) Integer> maxDistance,
            @RequestParam("maxPrice") Optional<@Min(0) Integer> maxPrice) {

        List<RestaurantSearchResultDto> results =  restaurantService.searchRestaurants(
                name.orElse(null), cuisine.orElse(null), minCustomerRating.orElse(null),
                maxDistance.orElse(null), maxPrice.orElse(null)
        );

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);

    }

}
