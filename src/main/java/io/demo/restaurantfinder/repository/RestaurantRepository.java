package io.demo.restaurantfinder.repository;

import io.demo.restaurantfinder.entity.Restaurant;
import io.demo.restaurantfinder.model.RestaurantSearchResultDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameIgnoreCase(String name);

    @Query("""
              SELECT new io.demo.restaurantfinder.model.RestaurantSearchResultDto(
                r.name, r.customerRating, r.distance, r.price, c.name)
              FROM Restaurant r JOIN r.cuisine c
              WHERE (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT(:name, '%')))
                AND (:cuisine IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT(:cuisine, '%')))
                AND (:minCustomerRating IS NULL OR r.customerRating >= :minCustomerRating)
                AND (:maxDistance IS NULL OR r.distance <= :maxDistance)
                AND (:maxPrice IS NULL OR r.price <= :maxPrice)
              ORDER BY r.distance ASC, r.customerRating DESC, r.price ASC, r.name ASC
            """)
    List<RestaurantSearchResultDto> searchRestaurants(String name,
                                                      String cuisine,
                                                      Integer minCustomerRating,
                                                      Integer maxDistance,
                                                      Integer maxPrice,
                                                      Pageable pageable);
}
