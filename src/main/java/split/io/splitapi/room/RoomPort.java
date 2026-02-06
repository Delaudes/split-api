package split.io.splitapi.room;

import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;

public interface RoomPort {

    void create(Room room);
    Room fetch(String id);
    void addPayer(String roomId, Payer payer);
    void addExpense(String payerId, Expense expense);
    void deleteExpense(String id);
}
