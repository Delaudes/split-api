package split.io.splitapi.room;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.room.adapters.FakeRoomAdapter;
import split.io.splitapi.room.models.*;
import split.io.splitapi.room.models.inputs.*;
import split.io.splitapi.room.models.outputs.*;
import split.io.splitapi.uuid.FakeUuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomFacadeTests {

    private RoomFacade roomFacade;
    private FakeRoomAdapter fakeRoomAdapter;
    private FakeUuidGenerator fakeUuidGenerator;

    @BeforeEach
    void setUp() {
        fakeRoomAdapter = new FakeRoomAdapter();
        fakeUuidGenerator = new FakeUuidGenerator();
        RoomMapper roomMapper = new RoomMapper();
        RoomService roomService = new RoomService(fakeRoomAdapter);
        roomFacade = new RoomFacade(roomService, roomMapper, fakeUuidGenerator);
    }

    @Test
    void shouldCreateRoomWithGeneratedId() {
        // Given
        String roomName = "fake-room-name";
        Room expectedRoom = new Room(fakeUuidGenerator.uuid, roomName, new ArrayList<>());
        CreateRoomRequest request = new CreateRoomRequest(roomName);

        // When
        CreateRoomResponse response = roomFacade.create(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(expectedRoom, fakeRoomAdapter.room);
    }

    @Test
    void shouldFetchRoomById() {
        // Given
        String roomId = "fake-room-id";
        fakeRoomAdapter.room = createRoom(roomId);
        FetchRoomResponse expectedRoom = createExpectedRoom(roomId);

        // When
        FetchRoomResponse response = roomFacade.fetch(roomId);

        // Then
        assertEquals(expectedRoom,response);
        assertEquals(roomId, fakeRoomAdapter.roomId);
    }

    @Test
    void shouldAddPayerToRoom() {
        // Given
        String roomId = "fake-room-id";
        String payerName = "fake-payer-name";
        AddPayerRequest request = new AddPayerRequest(roomId, payerName);
        Payer expectedPayer = new Payer(fakeUuidGenerator.uuid, payerName, new ArrayList<>());

        // When
        AddPayerResponse response = roomFacade.addPayer(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(roomId, fakeRoomAdapter.roomId);
        assertEquals(expectedPayer, fakeRoomAdapter.newPayer);
    }

    @Test
    void shouldAddExpenseToPayer() {
        // Given
        String payerId = "fake-payer-id";
        String expenseDescription = "fake-expense-description";
        BigDecimal expenseAmount = new BigDecimal("99.99");
        AddExpenseRequest request = new AddExpenseRequest(payerId, expenseDescription, expenseAmount);
        Expense expectedExpense = new Expense(fakeUuidGenerator.uuid, expenseDescription, expenseAmount, false, new ArrayList<>());

        // When
        AddExpenseResponse response = roomFacade.addExpense(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(payerId, fakeRoomAdapter.payerId);
        assertEquals(expectedExpense, fakeRoomAdapter.newExpense);
    }

    @Test
    void shouldDeleteExpenseById() {
        // Given
        String expenseId = "fake-expense-id";

        // When
        roomFacade.deleteExpense(expenseId);

        // Then
        assertEquals(expenseId, fakeRoomAdapter.expenseId);
    }

    @Test
    void shouldDeleteAllExpensesByRoomId() {
        // Given
        String roomId = "fake-room-id";

        // When
        roomFacade.deleteAllExpenses(roomId);

        // Then
        assertEquals(roomId, fakeRoomAdapter.roomId);
    }

    @Test
    void shouldEditRoomName() {
        // Given
        String roomId = "fake-room-id";
        String newRoomName = "new-fake-room-name";

        // When
        roomFacade.editRoomName(roomId, new EditRoomRequest(newRoomName));

        // Then
        assertEquals(roomId, fakeRoomAdapter.roomId);
        assertEquals(newRoomName, fakeRoomAdapter.newRoomName);
    }

    @Test
    void shouldEditPayerName() {
        // Given
        String payerId = "fake-payer-id";
        String newPayerName = "new-fake-payer-name";

        // When
        roomFacade.editPayerName(payerId, new EditPayerRequest(newPayerName));

        // Then
        assertEquals(payerId, fakeRoomAdapter.payerId);
        assertEquals(newPayerName, fakeRoomAdapter.newPayerName);
    }

    @Test
    void shouldDeletePayerById() {
        // Given
        String payerId = "fake-payer-id";

        // When
        roomFacade.deletePayer(payerId);

        // Then
        assertEquals(payerId, fakeRoomAdapter.payerId);
    }

    @Test
    void shouldDeleteRoomById() {
        // Given
        String roomId = "fake-room-id";

        // When
        roomFacade.deleteRoom(roomId);

        // Then
        assertEquals(roomId, fakeRoomAdapter.roomId);
    }

    @Test
    void shouldFetchRoomHistoryById() {
        // Given
        String roomId = "fake-room-id";
        fakeRoomAdapter.room = createRoom(roomId);
        FetchRoomResponse expectedRoom = createExpectedRoomHistory(roomId);

        // When
        FetchRoomResponse response = roomFacade.fetchHistory(roomId);

        // Then
        assertEquals(expectedRoom,response);
        assertEquals(roomId, fakeRoomAdapter.roomId);
    }

    @Test
    void shouldAddExpensePayer() {
        // Given
        String expenseId = "fake-expense-id";
        String payerId = "fake-payer-id";
        ExcludeExpensePayerRequest request = new ExcludeExpensePayerRequest(expenseId, payerId);

        // When
        roomFacade.excludeExpensePayer(request);

        // Then
        assertEquals(expenseId, fakeRoomAdapter.expenseId);
        assertEquals(payerId, fakeRoomAdapter.payerId);
    }

    @Test
    void shouldDeleteExpensePayer() {
        // Given
        String expenseId = "fake-expense-id";
        String payerId = "fake-payer-id";

        // When
        roomFacade.includeExpensePayer(expenseId, payerId);

        // Then
        assertEquals(expenseId, fakeRoomAdapter.expenseId);
        assertEquals(payerId, fakeRoomAdapter.payerId);
    }

    private static @NonNull Room createRoom(String roomId) {
        Payer payer1 = new Payer("payer-1", "Alice", new ArrayList<>());
        Expense expense1 = new Expense("expense-1-1", "Restaurant", new BigDecimal("45.50"), true, new ArrayList<>());
        expense1.addExcludedPayer(payer1.id());
        payer1.addExpense(expense1);
        payer1.addExpense(new Expense("expense-1-2", "Cinéma", new BigDecimal("12.00"), false, new ArrayList<>()));
        payer1.addExpense(new Expense("expense-1-3", "Essence", new BigDecimal("60.75"), false, new ArrayList<>()));

        Payer payer2 = new Payer("payer-2", "Bob", new ArrayList<>());
        Expense expense2 = new Expense("expense-2-3", "Pharmacie", new BigDecimal("18.30"), false, new ArrayList<>());
        expense2.addExcludedPayer(payer1.id());
        payer2.addExpense(new Expense("expense-2-1", "Hôtel", new BigDecimal("120.00"), false, new ArrayList<>()));
        payer2.addExpense(new Expense("expense-2-2", "Supermarché", new BigDecimal("35.80"), true, new ArrayList<>()));
        payer2.addExpense(expense2);

        Payer payer3 = new Payer("payer-3", "Charlie", new ArrayList<>());
        payer3.addExpense(new Expense("expense-3-1", "Train", new BigDecimal("85.00"), false, new ArrayList<>()));
        payer3.addExpense(new Expense("expense-3-2", "Café", new BigDecimal("8.50"), false, new ArrayList<>()));
        payer3.addExpense(new Expense("expense-3-3", "Musée", new BigDecimal("15.00"), false, new ArrayList<>()));

        Room room = new Room(roomId, "fake-room-name", new ArrayList<>());
        room.addPayer(payer1);
        room.addPayer(payer2);
        room.addPayer(payer3);
        return room;
    }

    private static @NonNull FetchRoomResponse createExpectedRoom(String roomId) {
        FetchPayerResponse payer1 = new FetchPayerResponse("payer-1", "Alice", new ArrayList<>());
        payer1.expenses().add(new FetchExpenseResponse("expense-1-2", "Cinéma", new BigDecimal("12.00"), new ArrayList<>()));
        payer1.expenses().add(new FetchExpenseResponse("expense-1-3", "Essence", new BigDecimal("60.75"), new ArrayList<>()));

        FetchPayerResponse payer2 = new FetchPayerResponse("payer-2", "Bob", new ArrayList<>());
        payer2.expenses().add(new FetchExpenseResponse("expense-2-1", "Hôtel", new BigDecimal("120.00"), new ArrayList<>()));
        FetchExpenseResponse expense2 = new FetchExpenseResponse("expense-2-3", "Pharmacie", new BigDecimal("18.30"), new ArrayList<>());
        expense2.excludedPayersId().add(payer1.id());
        payer2.expenses().add(expense2);

        FetchPayerResponse payer3 = new FetchPayerResponse("payer-3", "Charlie", new ArrayList<>());
        payer3.expenses().add(new FetchExpenseResponse("expense-3-1", "Train", new BigDecimal("85.00"), new ArrayList<>()));
        payer3.expenses().add(new FetchExpenseResponse("expense-3-2", "Café", new BigDecimal("8.50"), new ArrayList<>()));
        payer3.expenses().add(new FetchExpenseResponse("expense-3-3", "Musée", new BigDecimal("15.00"), new ArrayList<>()));

        FetchRoomResponse room = new FetchRoomResponse(roomId, "fake-room-name", new ArrayList<>());
        room.payers().add(payer1);
        room.payers().add(payer2);
        room.payers().add(payer3);
        return room;
    }

    private static @NonNull FetchRoomResponse createExpectedRoomHistory(String roomId) {
        FetchPayerResponse payer1 = new FetchPayerResponse("payer-1", "Alice", new ArrayList<>());
        FetchExpenseResponse expense1 = new FetchExpenseResponse("expense-1-1", "Restaurant", new BigDecimal("45.50"), new ArrayList<>());
        expense1.excludedPayersId().add(payer1.id());
        payer1.expenses().add(expense1);

        FetchPayerResponse payer2 = new FetchPayerResponse("payer-2", "Bob", new ArrayList<>());
        payer2.expenses().add(new FetchExpenseResponse("expense-2-2", "Supermarché", new BigDecimal("35.80"), new ArrayList<>()));

        FetchRoomResponse room = new FetchRoomResponse(roomId, "fake-room-name", new ArrayList<>());
        room.payers().add(payer1);
        room.payers().add(payer2);
        return room;
    }
}
