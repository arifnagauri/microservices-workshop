package io.arif.moviecatalogservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.arif.moviecatalogservice.model.CatalogItem;
import io.arif.moviecatalogservice.model.Movie;
import io.arif.moviecatalogservice.model.Rating;
import io.arif.moviecatalogservice.model.UserRatings;
import io.arif.moviecatalogservice.service.MovieInfoService;
import io.arif.moviecatalogservice.service.UserRatingsService;
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

    private final UserRatingsService userRatingsService;
    private final MovieInfoService movieInfoService;

    @Autowired
    MovieCatalogController(final UserRatingsService userRatingsService, final MovieInfoService movieInfoService) {
        this.userRatingsService = userRatingsService;
        this.movieInfoService = movieInfoService;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getMovieCatalog(@PathVariable("userId") String userId) {

        UserRatings userRatings = userRatingsService.getUserRatings(userId);

        return userRatings.getRatings().stream().map(rating -> movieInfoService.getCatalogItem(rating)).collect(Collectors.toList());
    }
}
