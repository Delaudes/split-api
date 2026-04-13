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
    public String newPayerName;

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

    @Override
    public void editPayerName(String id, String name) {
        this.payerId = id;
        this.newPayerName = name;
    }

    @Override
    public void deletePayer(String id) {
        this.payerId = id;
    }

    @Override
    public void deleteRoom(String id) {
        this.roomId = id;
    }

    @Override
    public void archiveAllExpenses(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void addExpensePayer(String expenseId, String payerId) {
        this.expenseId = expenseId;
        this.payerId = payerId;
    }

    @Override
    public void deleteExpensePayer(String expenseId, String payerId) {
        this.expenseId = expenseId;
        this.payerId = payerId;
    }
}
