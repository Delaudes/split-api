package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.RoomEntity;

import java.util.Optional;

public interface RoomRepository {
    void save(RoomEntity roomEntity);
    Optional<RoomEntity> findById(String id);
    void deleteById(String id);
}
