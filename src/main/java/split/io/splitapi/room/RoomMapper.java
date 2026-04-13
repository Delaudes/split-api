package split.io.splitapi.room;

import split.io.splitapi.room.models.*;
import split.io.splitapi.room.models.inputs.AddExpenseRequest;
import split.io.splitapi.room.models.inputs.AddPayerRequest;
import split.io.splitapi.room.models.inputs.CreateRoomRequest;
import split.io.splitapi.room.models.outputs.*;

import java.util.ArrayList;
import java.util.List;

public class RoomMapper {

    public CreateRoomResponse toCreateRoomResponse(Room room) {
        return new CreateRoomResponse(room.id());
    }

    public FetchRoomResponse toFetchRoomResponse(Room room) {
        List<FetchPayerResponse> payerResponses = room.payers().stream()
                .map(payer -> new FetchPayerResponse(payer.id(), payer.name(),
                        payer.expenses().stream()
                                .map(expense -> new FetchExpenseResponse(expense.id(), expense.description(), expense.amount(), expense.excludedPayersId()))
                                .toList()))
                .toList();
        return new FetchRoomResponse(room.id(), room.name(), payerResponses);
    }

    public AddPayerResponse toAddPayerResponse(Payer payer) {
        return new AddPayerResponse(payer.id());
    }

    public AddExpenseResponse toAddExpenseResponse(Expense expense) {
        return new AddExpenseResponse(expense.id());
    }

    public Room toRoom(CreateRoomRequest request, String id) {
        return new Room(id, request.name(), new ArrayList<>());
    }

    public Payer toPayer(AddPayerRequest request, String id) {
        return new Payer(id, request.payerName(), new ArrayList<>());
    }

    public Expense toExpense(AddExpenseRequest request, String id) {
        return new Expense(id, request.expenseDescription(), request.expenseAmount(), false, new ArrayList<>());
    }

}
