package split.io.splitapi.room.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import split.io.splitapi.room.models.entities.ExpenseEntity;

public interface JpaExpenseRepository extends Repository<ExpenseEntity, String>, ExpenseRepository {

    @Modifying
    @Transactional
    @Query("DELETE FROM ExpenseEntity e WHERE e.payerId IN (SELECT p.id FROM PayerEntity p WHERE p.roomId = :roomId)")
    void deleteByRoomId(@Param("roomId") String roomId);
}
