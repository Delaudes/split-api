package split.io.splitapi.room;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;

@RequiredArgsConstructor
public class RoomService {

    private final RoomPort roomPort;

    public void createRoom(Room room) {
        roomPort.create(room);
    }

    public Room fetchRoom(String id) {
        return roomPort.fetch(id);
    }

    public void addPayer(String roomId, Payer payer) {
        roomPort.addPayer(roomId, payer);
    }

    public void addExpense(String payerId, Expense expense) {
        roomPort.addExpense(payerId, expense);
    }

    public void deleteExpense(String id) {
        roomPort.deleteExpense(id);
    }
}
