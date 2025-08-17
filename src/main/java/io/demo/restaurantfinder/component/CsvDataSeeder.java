package io.demo.restaurantfinder.component;

import com.opencsv.CSVReader;
import io.demo.restaurantfinder.entity.Cuisine;
import io.demo.restaurantfinder.entity.Restaurant;
import io.demo.restaurantfinder.repository.CuisineRepository;
import io.demo.restaurantfinder.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Component to seed initial data from CSV files into the database.
 *
 */
@Component
public class CsvDataSeeder {

    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;
    private final ResourceLoader resourceLoader;

    public CsvDataSeeder(CuisineRepository cuisineRepository,
                         RestaurantRepository restaurantRepository,
                         ResourceLoader resourceLoader) {
        this.cuisineRepository = cuisineRepository;
        this.restaurantRepository = restaurantRepository;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() throws Exception {
        loadCuisines("classpath:data/cuisines.csv");
        loadRestaurants("classpath:data/restaurants.csv");
    }

    private void loadCuisines(String loc) throws Exception {

        List<String[]> lines = readCsv(loc);

        if (lines.isEmpty()) {
            return;
        }

        final int[] totalSaved = {0};

        lines.forEach(line -> {
            long id = Integer.parseInt(line[0]);
            String name = line[1];
            totalSaved[0]++;
            cuisineRepository.findById(id).orElseGet(() -> cuisineRepository.save(new Cuisine(id, name)));
        });

        System.out.println("Cuisines saved: " + totalSaved[0]);

    }

    private void loadRestaurants(String loc) throws Exception {

        List<String[]> lines = readCsv(loc);

        if (lines.isEmpty()) {
            return;
        }

        final int[] totalSaved = {0};

        lines.forEach(line -> {
            String name = line[0];
            int customerRating = Integer.parseInt(line[1]);
            int distance = Integer.parseInt(line[2]);
            int price = Integer.parseInt(line[3]);
            long cuisineId = Integer.parseInt(line[4]);

            restaurantRepository.findByNameIgnoreCase(name).orElseGet(() -> {
                Cuisine cuisine = cuisineRepository.findById(cuisineId).orElse(null);
                Restaurant restaurant = new Restaurant(name, customerRating, distance, price, cuisine);
                totalSaved[0]++;
                return restaurantRepository.save(restaurant);
            });

        });

        System.out.println("Restaurants saved: " + totalSaved[0]);

    }

    public List<String[]> readCsv(String loc) throws Exception {
        List<String[]> lines = new ArrayList<>();
        Resource resource = resourceLoader.getResource(loc);

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            // skip header
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

}
