package io.demo.restaurantfinder.service;

import io.demo.restaurantfinder.model.RestaurantSearchResultDto;

import java.util.List;

public interface RestaurantService {

    List<RestaurantSearchResultDto> searchRestaurants(
            String name,
            String cuisine,
            Integer minCustomerRating,
            Integer maxDistance,
            Integer maxPrice
    );

}
