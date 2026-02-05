package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.ExpenseEntity;

public class FakeExpenseRepository implements ExpenseRepository {

    public ExpenseEntity savedExpense;
    public String deletedExpenseId;

    @Override
    public void save(ExpenseEntity expenseEntity) {
        this.savedExpense = expenseEntity;
    }

    @Override
    public void deleteById(String id) {
        this.deletedExpenseId = id;
    }
}
