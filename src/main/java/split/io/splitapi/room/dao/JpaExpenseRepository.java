package split.io.splitapi.room.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.room.models.entities.ExpenseEntity;

public interface JpaExpenseRepository extends Repository<ExpenseEntity, String>, ExpenseRepository {
}
