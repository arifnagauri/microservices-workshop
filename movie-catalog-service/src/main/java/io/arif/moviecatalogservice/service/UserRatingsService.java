package io.arif.moviecatalogservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.arif.moviecatalogservice.model.Rating;
import io.arif.moviecatalogservice.model.UserRatings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingsService {

    private final RestTemplate restTemplate;

    @Autowired
    UserRatingsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackForUserRatings")
    public UserRatings getUserRatings(String userId) {
        UserRatings userRatings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
                UserRatings.class);
        return userRatings;
    }

    private UserRatings getFallbackForUserRatings(String userId) {
        UserRatings userRatings = new UserRatings();
        userRatings.setUserId(userId);
        userRatings.setRatings(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRatings;
    }
}
//    private final WebClient.Builder webClientBuilder;

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/\" + rating.getMovieId()")
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();

