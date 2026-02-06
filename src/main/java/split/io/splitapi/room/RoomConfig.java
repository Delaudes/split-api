package split.io.splitapi.room;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import split.io.splitapi.uuid.UuidGenerator;

@Configuration
public class RoomConfig {

    @Bean
    public RoomMapper roomPresenter() {
        return new RoomMapper();
    }

    @Bean
    public RoomFacade roomFacade(RoomService service,
                                  RoomMapper presenter,
                                  UuidGenerator uuidGenerator) {
        return new RoomFacade(service, presenter, uuidGenerator);
    }

    @Bean
    public RoomService roomService(RoomPort roomPort) {
        return new RoomService(roomPort);
    }
}
