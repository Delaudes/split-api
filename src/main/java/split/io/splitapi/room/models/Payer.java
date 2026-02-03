package split.io.splitapi.room.models;

import java.util.ArrayList;

public record Payer(String id, String name, ArrayList<Expense> expenses) {

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public void deleteExpense(String id) {
        expenses.removeIf(expense -> expense.id().equals(id));
    }
}
