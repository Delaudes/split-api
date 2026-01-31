package split.io.splitapi.room;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.uuid.UuidGenerator;

@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomPresenter roomPresenter;
    private final UuidGenerator uuidGenerator;

    public CreateRoomResponse create(CreateRoomRequest request) {
        String id = uuidGenerator.generate();
        Room room = new Room(id, request.name());
        roomRepository.create(room);
        return roomPresenter.toCreateRoomResponse(room);
    }
}
