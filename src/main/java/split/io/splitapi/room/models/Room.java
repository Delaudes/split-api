package split.io.splitapi.room.models;

import java.util.ArrayList;

public record Room(String id, String name, ArrayList<Payer> payers) {

    public void addPayer(Payer payer) {
        this.payers.add(payer);
    }

    public void addExpense(String payerId, Expense expense) {
        for (Payer payer : payers) {
            if (payer.id().equals(payerId)) {
                payer.addExpense(expense);
                return;
            }
        }
    }

    public void deleteExpense(String id) {
        for (Payer payer : payers) {
            payer.deleteExpense(id);
        }
    }

    public void deleteAllExpenses() {
        for (Payer payer : payers) {
            payer.deleteExpenses();
        }
    }
}
