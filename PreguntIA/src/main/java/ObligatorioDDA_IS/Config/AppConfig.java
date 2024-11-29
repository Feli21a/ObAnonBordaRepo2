package ObligatorioDDA_IS.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import ObligatorioDDA_IS.Services.RankingSystem;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RankingSystem rankingSystem() {
        return new RankingSystem();
    }
}