package split.io.splitapi.room;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.room.models.*;
import split.io.splitapi.room.models.inputs.AddExpenseRequest;
import split.io.splitapi.room.models.inputs.AddPayerRequest;
import split.io.splitapi.room.models.inputs.CreateRoomRequest;
import split.io.splitapi.room.models.outputs.AddExpenseResponse;
import split.io.splitapi.room.models.outputs.AddPayerResponse;
import split.io.splitapi.room.models.outputs.CreateRoomResponse;
import split.io.splitapi.room.models.outputs.FetchRoomResponse;
import split.io.splitapi.uuid.UuidGenerator;
import java.util.ArrayList;

@RequiredArgsConstructor
public class RoomService {

    private final RoomGateway roomGateway;
    private final RoomPresenter roomPresenter;
    private final UuidGenerator uuidGenerator;

    public CreateRoomResponse create(CreateRoomRequest request) {
        String id = uuidGenerator.generate();
        Room room = new Room(id, request.name(), new ArrayList<>());
        roomGateway.create(room);
        return roomPresenter.toCreateRoomResponse(room);
    }

    public FetchRoomResponse fetch(String id) {
        Room room = roomGateway.fetch(id);
        return roomPresenter.toFetchRoomResponse(room);
    }

    public AddPayerResponse addPayer(AddPayerRequest request ) {
        String payerId = uuidGenerator.generate();
        Payer payer = new Payer(payerId, request.payerName(), new ArrayList<>());
        roomGateway.addPayer(request.roomId(), payer);
        return roomPresenter.toAddPayerResponse(payer);
    }

    public AddExpenseResponse addExpense(AddExpenseRequest request ) {
        String expenseId = uuidGenerator.generate();
        Expense expense = new Expense(expenseId, request.expenseDescription(), request.expenseAmount());
        roomGateway.addExpense(request.payerId(), expense);
        return roomPresenter.toAddExpenseResponse(expense);
    }

    public void deleteExpense(String id) {
        roomGateway.deleteExpense(id);
    }
}
