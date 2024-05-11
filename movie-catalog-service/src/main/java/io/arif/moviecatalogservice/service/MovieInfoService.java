package io.arif.moviecatalogservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.arif.moviecatalogservice.model.CatalogItem;
import io.arif.moviecatalogservice.model.Movie;
import io.arif.moviecatalogservice.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfoService {

    private final RestTemplate restTemplate;

    @Autowired
    MovieInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackForCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
    }

    private CatalogItem getFallbackForCatalogItem(Rating rating) {
        return new CatalogItem("Movie name Not Found", "", rating.getRating());
    }
}
