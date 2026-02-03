package split.io.splitapi.room;

import split.io.splitapi.room.models.*;

import java.util.List;

public class RoomPresenter {

    public CreateRoomResponse toCreateRoomResponse(Room room) {
        return new CreateRoomResponse(room.id());
    }

    public FetchRoomResponse toFetchRoomResponse(Room room) {
        List<FetchPayerResponse> payerResponses = room.payers().stream()
                .map(payer -> new FetchPayerResponse(payer.id(), payer.name(),
                        payer.expenses().stream()
                                .map(expense -> new FetchExpenseResponse(expense.id(), expense.description(), expense.amount()))
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
}
