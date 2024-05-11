package io.arif.moviecatalogservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.arif.moviecatalogservice.model.CatalogItem;
import io.arif.moviecatalogservice.model.Movie;
import io.arif.moviecatalogservice.model.Rating;
import io.arif.moviecatalogservice.model.UserRatings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    private final RestTemplate restTemplate;

    @Autowired
    MovieCatalogController(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getMovieCatalog(@PathVariable("userId") String userId) {

        UserRatings userRatings = getUserRatings(userId);

        return userRatings.getRatings().stream().map(rating -> getCatalogItem(rating)).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackForUserRatings")
    private UserRatings getUserRatings(String userId) {
        UserRatings userRatings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
                UserRatings.class);
        return userRatings;
    }

    @HystrixCommand(fallbackMethod = "getFallbackForCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
    }

    public UserRatings getFallbackForUserRatings(String userId) {
        UserRatings userRatings = new UserRatings();
        userRatings.setUserId(userId);
        userRatings.setRatings(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRatings;
    }

    public CatalogItem getFallbackForCatalogItem(Rating rating) {
        return new CatalogItem("Movie name Not Found", "", rating.getRating());
    }
}

//    private final WebClient.Builder webClientBuilder;

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/\" + rating.getMovieId()")
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();
