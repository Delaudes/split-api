package split.io.splitapi.room.models;

import java.util.ArrayList;

public record Payer(String id, String name, ArrayList<Expense> expenses) {

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public void deleteArchivedExpenses() {
        this.expenses.removeIf(Expense::archived);
    }

    public void deleteNotArchivedExpenses() {
        this.expenses.removeIf(expense -> !expense.archived());
    }

    public boolean hasEmptyExpenses() {
        return this.expenses.isEmpty();
    }
}
