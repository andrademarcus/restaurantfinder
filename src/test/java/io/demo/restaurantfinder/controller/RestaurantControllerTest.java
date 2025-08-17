package io.demo.restaurantfinder.controller;

import io.demo.restaurantfinder.model.RestaurantSearchResultDto;
import io.demo.restaurantfinder.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;
    private RestaurantController restaurantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantController = new RestaurantController(restaurantService);
    }

    @Test
    void searchRestaurants_returnsResults_whenFound() {
        RestaurantSearchResultDto dto = new RestaurantSearchResultDto(
                "Pizza Place", 4,
                10, 20, "Italian");
        List<RestaurantSearchResultDto> results = List.of(dto);

        when(restaurantService.searchRestaurants(
                any(), any(), any(), any(), any())
        ).thenReturn(results);

        ResponseEntity<List<RestaurantSearchResultDto>> response = restaurantController.searchRestaurants(
                Optional.of("Pizza"), Optional.of("Italian"),
                Optional.of(4), Optional.of(10), Optional.of(20)
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(restaurantService, times(1)).searchRestaurants(
                "Pizza", "Italian", 4, 10, 20
        );
    }

    @Test
    void searchRestaurants_returnsNoContent_whenNoResults() {
        when(restaurantService.searchRestaurants(
                any(), any(), any(), any(), any())
        ).thenReturn(Collections.emptyList());

        ResponseEntity<List<RestaurantSearchResultDto>> response = restaurantController.searchRestaurants(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty()
        );

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(restaurantService, times(1)).searchRestaurants(
                null, null, null, null, null
        );
    }
}