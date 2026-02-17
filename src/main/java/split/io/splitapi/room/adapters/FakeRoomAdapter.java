package split.io.splitapi.room.adapters;

import split.io.splitapi.room.RoomPort;
import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;

import java.util.ArrayList;

public class FakeRoomAdapter implements RoomPort {

    public Room room = new Room("fake-room-id", "fake-room-name", new ArrayList<>());
    public String roomId;
    public String payerId;
    public String expenseId;
    public Payer newPayer;
    public Expense newExpense;
    public String newRoomName;

    @Override
    public void create(Room room) {
        this.room = room;
    }

    @Override
    public Room fetch(String id) {
        this.roomId = id;
        return room;
    }

    @Override
    public void addPayer(String roomId, Payer payer) {
        this.roomId = roomId;
        this.newPayer = payer;
    }

    @Override
    public void addExpense(String payerId, Expense expense) {
        this.payerId = payerId;
        this.newExpense = expense;
    }

    @Override
    public void deleteExpense(String id) {
        this.expenseId = id;
    }

    @Override
    public void deleteAllExpenses(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void editRoomName(String id, String name) {
        this.roomId = id;
        this.newRoomName = name;
    }
}
