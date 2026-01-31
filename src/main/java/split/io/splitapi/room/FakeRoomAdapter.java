package split.io.splitapi.room;

public class FakeRoomAdapter implements RoomRepository {

    public Room room;

    @Override
    public void create(Room room) {
        this.room = room;
    }
}
