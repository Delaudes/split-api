package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.ExpenseEntity;

public interface ExpenseRepository {
    void save(ExpenseEntity expenseEntity);
    void deleteById(String id);
    void deleteByRoomId(String roomId);
}
