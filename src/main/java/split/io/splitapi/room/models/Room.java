package split.io.splitapi.room.models;

import java.util.ArrayList;

public record Room(String id, String name, ArrayList<Payer> payers) {

    public void addPayer(Payer payer) {
        this.payers.add(payer);
    }

    public void deleteArchivedExpenses() {
        for (Payer payer : this.payers) {
            payer.deleteArchivedExpenses();
        }
    }

    public void deleteNotArchivedExpenses() {
        this.payers.removeIf(payer -> {
            payer.deleteNotArchivedExpenses();
            return payer.hasEmptyExpenses();
        });
    }
}
