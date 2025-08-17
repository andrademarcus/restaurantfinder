package io.demo.restaurantfinder.service.impl;

import io.demo.restaurantfinder.model.RestaurantSearchResultDto;
import io.demo.restaurantfinder.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantService = new RestaurantServiceImpl(restaurantRepository);
        ReflectionTestUtils.setField(restaurantService, "searchMaxResults", 5);
    }

    @Test
    void searchRestaurants_returnsResults_whenFound() {
        RestaurantSearchResultDto dto = new RestaurantSearchResultDto(
                "Pizza Place", 4,
                10, 20, "Italian");
        List<RestaurantSearchResultDto> results = List.of(dto);

        when(restaurantRepository.search(
                anyString(), anyString(), anyInt(), anyInt(), anyInt(), any())
        ).thenReturn(results);

        List<RestaurantSearchResultDto> found = restaurantService.searchRestaurants(
                "Pizza", "Italian", 4, 10, 20
        );

        assertNotNull(found);
        assertEquals(1, found.size());
        verify(restaurantRepository, times(1)).search(
                eq("Pizza"), eq("Italian"), eq(4), eq(10), eq(20), any(Pageable.class)
        );
    }

    @Test
    void searchRestaurants_returnsEmptyList_whenNotFound() {
        when(restaurantRepository.search(
                anyString(), anyString(), anyInt(), anyInt(), anyInt(), any())
        ).thenReturn(Collections.emptyList());

        List<RestaurantSearchResultDto> found = restaurantService.searchRestaurants(
                null, null, null, null, null
        );

        assertNotNull(found);
        assertTrue(found.isEmpty());
        verify(restaurantRepository, times(1)).search(
                eq(null), eq(null), eq(null), eq(null), eq(null), any(Pageable.class)
        );
    }
}