package split.io.splitapi.room;

public class RoomPresenter {

    public CreateRoomResponse toCreateRoomResponse(Room room) {
        return new CreateRoomResponse(room.getId());
    }
}
