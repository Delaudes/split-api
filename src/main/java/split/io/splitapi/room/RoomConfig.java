package split.io.splitapi.room;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import split.io.splitapi.uuid.UuidGenerator;

@Configuration
public class RoomConfig {

    @Bean
    public RoomPresenter roomPresenter() {
        return new RoomPresenter();
    }

    @Bean
    public RoomService roomService(RoomGateway repository,
                                   RoomPresenter presenter,
                                   UuidGenerator uuidGenerator) {
        return new RoomService(repository, presenter, uuidGenerator);
    }
}
