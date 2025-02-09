package io.javabrains.movie_catalog_service.resources;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.javabrains.movie_catalog_service.models.CatalogItem;
import io.javabrains.movie_catalog_service.models.Movie;
import io.javabrains.movie_catalog_service.models.Rating;
import io.javabrains.movie_catalog_service.models.UserRating;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackMethod")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        UserRating ratings = restTemplate.getForObject(
                "http://rating-data-service/ratingsdata/users/" + userId,
                UserRating.class
        );

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject(
                    "http://movie-info-service/movies/" + rating.getMovieId(),
                    Movie.class
            );
            return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
        }).collect(Collectors.toList());
    }

    public List<CatalogItem> fallbackMethod(String userId, Throwable throwable) {
        System.err.println("Fallback triggered for userId: " + userId + ". Error: " + throwable.getMessage());
        return Collections.singletonList(
                new CatalogItem("Fallback Movie", "No description available", 0)
        );
    }

}

