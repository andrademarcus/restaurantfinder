package io.demo.restaurantfinder.model;

public record RestaurantSearchResultDto(
        String name,
        int customerRating,
        int distance,
        int price,
        String cuisine
) {}