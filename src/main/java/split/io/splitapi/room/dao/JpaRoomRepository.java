package split.io.splitapi.room.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.room.models.entities.RoomEntity;

public interface JpaRoomRepository extends Repository<RoomEntity, String>, RoomRepository {
}
