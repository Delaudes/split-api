package split.io.splitapi.room;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import split.io.splitapi.room.adapters.FakeRoomAdapter;
import split.io.splitapi.uuid.UuidGenerator;

@Configuration
public class RoomConfig {

    @Bean
    public RoomRepository roomRepository() {
        return new FakeRoomAdapter();
    }

    @Bean
    public RoomPresenter roomPresenter() {
        return new RoomPresenter();
    }

    @Bean
    public RoomService roomService(RoomRepository repository,
                                    RoomPresenter presenter,
                                    UuidGenerator uuidGenerator) {
        return new RoomService(repository, presenter, uuidGenerator);
    }
}
