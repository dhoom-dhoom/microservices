package io.java.rating.resources;
import java.util.List;
import java.util.Arrays;

import io.java.rating.models.Rating;
import io.java.rating.models.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

    @RequestMapping("/movies/{movieId}")
    public Rating getMovieRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/users/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(
                new Rating("100",4),
                new Rating("101",3)
        );

        UserRating userRating = new UserRating();
        userRating.setUserRating(ratings);
        return userRating;

    }

}