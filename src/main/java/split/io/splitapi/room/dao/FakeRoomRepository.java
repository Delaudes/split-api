package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.RoomEntity;

import java.util.Optional;

public class FakeRoomRepository implements RoomRepository {

    public RoomEntity savedRoom;
    public String findByIdParam;
    public RoomEntity roomToReturn;
    public String deletedRoomId;

    @Override
    public void save(RoomEntity roomEntity) {
        this.savedRoom = roomEntity;
    }

    @Override
    public Optional<RoomEntity> findById(String id) {
        this.findByIdParam = id;
        return Optional.ofNullable(roomToReturn);
    }

    @Override
    public void deleteById(String id) {
        this.deletedRoomId = id;
    }
}
