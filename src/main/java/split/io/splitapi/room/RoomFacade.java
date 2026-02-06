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

@RequiredArgsConstructor
public class RoomFacade {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final UuidGenerator uuidGenerator;

    public CreateRoomResponse create(CreateRoomRequest request) {
        String roomId = uuidGenerator.generate();
        Room room = roomMapper.toRoom(request, roomId);
        roomService.createRoom(room);
        return roomMapper.toCreateRoomResponse(room);
    }

    public FetchRoomResponse fetch(String id) {
        Room room = roomService.fetchRoom(id);
        return roomMapper.toFetchRoomResponse(room);
    }

    public AddPayerResponse addPayer(AddPayerRequest request ) {
        String payerId = uuidGenerator.generate();
        Payer payer = roomMapper.toPayer(request, payerId);
        roomService.addPayer(request.roomId(), payer);
        return roomMapper.toAddPayerResponse(payer);
    }

    public AddExpenseResponse addExpense(AddExpenseRequest request ) {
        String expenseId = uuidGenerator.generate();
        Expense expense = roomMapper.toExpense(request, expenseId);
        roomService.addExpense(request.payerId(), expense);
        return roomMapper.toAddExpenseResponse(expense);
    }

    public void deleteExpense(String id) {
        roomService.deleteExpense(id);
    }
}
