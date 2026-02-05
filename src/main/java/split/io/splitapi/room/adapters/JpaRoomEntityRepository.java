package split.io.splitapi.room.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import split.io.splitapi.room.models.entities.RoomEntity;

@Repository
public interface JpaRoomEntityRepository extends JpaRepository<RoomEntity, String> {
}
