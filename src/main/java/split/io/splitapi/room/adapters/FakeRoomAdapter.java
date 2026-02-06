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
        this.room.addPayer(payer);
    }

    @Override
    public void addExpense(String payerId, Expense expense) {
        this.payerId = payerId;
        this.room.addExpense(payerId, expense);
    }

    @Override
    public void deleteExpense(String id) {
        this.room.deleteExpense(id);
    }
}
