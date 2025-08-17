# Restaurant Finder

```
Main class: io.demo.restaurantfinder.RestaurantfinderApplication
Spring Boot: 3.5.x
Java: 17.x
Port: 8080
```

## Quick start

### 1) Build & test
`./gradlew clean test`

### 2) Run
`./gradlew bootRun`

## Endpoint(s)

Search Restaurants (all params are optional):

``GET /restaurants/search``

```
Query params:
- name: partial match (case-insensitive)
- cuisine: partial match (case-insensitive)
- minCustomerRating: integer [1..5]
- maxDistance: integer [1..10], miles
- maxPrice: integer [10..50]
```

### Example

```curl --location 'http://localhost:8080/restaurants/search?name=Del&cuisine=&minCustomerRating=1&maxDistance=10&maxPrice=20'```


## Validation & errors

- Invalid params → 400 Bad Request with details

Sample:

```json
{
  "timestamp": "2025-08-17T15:10:08.57556-03:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/restaurants/search",
  "details": [
    "searchRestaurants.maxDistance: must be greater than or equal to 0",
    "searchRestaurants.minCustomerRating: must be greater than or equal to 1"
  ]
}
```

