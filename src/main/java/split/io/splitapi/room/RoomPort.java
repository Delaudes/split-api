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
    void deleteAllExpenses(String roomId);
    void editRoomName(String id, String name);
    void editPayerName(String id, String name);
    void deletePayer(String id);
    void deleteRoom(String id);
    void archiveAllExpenses(String roomId);
    void addExpensePayer(String expenseId, String payerId);
    void deleteExpensePayer(String expenseId, String payerId);
}
