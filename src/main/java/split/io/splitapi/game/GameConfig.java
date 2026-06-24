package split.io.splitapi.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import split.io.splitapi.uuid.UuidGenerator;

@Configuration
public class GameConfig {

    @Bean
    public GameMapper gameMapper() {
        return new GameMapper();
    }

    @Bean
    public GameFacade gameFacade(GameService gameService,
                                 GameMapper gameMapper,
                                 UuidGenerator uuidGenerator) {
        return new GameFacade(gameService, gameMapper, uuidGenerator);
    }

    @Bean
    public GameService gameService(GamePort gamePort) {
        return new GameService(gamePort);
    }
}
