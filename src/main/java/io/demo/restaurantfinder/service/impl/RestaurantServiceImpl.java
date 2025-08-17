package io.demo.restaurantfinder.service.impl;

import io.demo.restaurantfinder.model.RestaurantSearchResultDto;
import io.demo.restaurantfinder.repository.RestaurantRepository;
import io.demo.restaurantfinder.service.RestaurantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Value("${restaurant.search.maxResults:5}")
    private int searchMaxResults;

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantSearchResultDto> searchRestaurants(
            String name, String cuisine, Integer minCustomerRating, Integer maxDistance, Integer maxPrice) {

        Pageable top5 = PageRequest.of(0, searchMaxResults);
        return restaurantRepository.searchRestaurants(name, cuisine, minCustomerRating, maxDistance, maxPrice, top5);

    }
}
