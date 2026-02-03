package split.io.splitapi.room;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.room.adapters.FakeRoomAdapter;
import split.io.splitapi.room.models.*;
import split.io.splitapi.uuid.FakeUuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomServiceTest {

    private RoomService roomService;
    private FakeRoomAdapter fakeRoomAdapter;
    private FakeUuidGenerator fakeUuidGenerator;

    @BeforeEach
    void setUp() {
        fakeRoomAdapter = new FakeRoomAdapter();
        fakeUuidGenerator = new FakeUuidGenerator();
        RoomPresenter roomPresenter = new RoomPresenter();
        roomService = new RoomService(fakeRoomAdapter, roomPresenter, fakeUuidGenerator);
    }

    @Test
    void shouldCreateRoomWithGeneratedId() {
        // Given
        String roomName = "fake-room-name";
        Room expectedRoom = new Room(fakeUuidGenerator.uuid, roomName, new ArrayList<>());
        CreateRoomRequest request = new CreateRoomRequest(roomName);

        // When
        CreateRoomResponse response = roomService.create(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(fakeRoomAdapter.room, expectedRoom);
    }

    @Test
    void shouldFetchRoomById() {
        // Given
        String roomId = "fake-room-id";
        fakeRoomAdapter.room = createRoom(roomId);
        FetchRoomResponse expectedRoom = createExpectedRoom(roomId);

        // When
        FetchRoomResponse response = roomService.fetch(roomId);

        // Then
        assertEquals(roomId, fakeRoomAdapter.roomId);
        assertEquals(response, expectedRoom);
    }

    @Test
    void shouldAddPayerToRoom() {
        // Given
        String roomId = "fake-room-id";
        String payerName = "fake-payer-name";
        AddPayerRequest request = new AddPayerRequest(roomId, payerName);
        Payer expectedPayer = new Payer(fakeUuidGenerator.uuid, payerName, new ArrayList<>());

        // When
        AddPayerResponse response = roomService.addPayer(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(roomId, fakeRoomAdapter.roomId);
        assertEquals(fakeRoomAdapter.room.payers().getFirst(), expectedPayer);
    }

    @Test
    void shouldAddExpenseToPayer() {
        // Given
        String payerId = "fake-payer-id";
        fakeRoomAdapter.room = createRoomWithPayer(payerId);
        String expenseDescription = "fake-expense-description";
        BigDecimal expenseAmount = new BigDecimal("99.99");
        AddExpenseRequest request = new AddExpenseRequest(payerId, expenseDescription, expenseAmount);
        Expense expectedExpense = new Expense(fakeUuidGenerator.uuid, expenseDescription, expenseAmount);

        // When
        AddExpenseResponse response = roomService.addExpense(request);

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(fakeRoomAdapter.room.payers().getFirst().expenses().getFirst(), expectedExpense);
    }

    @Test
    void shouldDeleteExpenseById() {
        // Given
        String expenseId = "fake-expense-id";
        fakeRoomAdapter.room = createRoomWithExpense(expenseId);

        // When
        roomService.deleteExpense(expenseId);

        // Then
        assertTrue(fakeRoomAdapter.room.payers().getFirst().expenses().isEmpty());
    }


    private static @NonNull Room createRoom(String roomId) {
        Payer payer1 = new Payer("payer-1", "Alice", new ArrayList<>());
        payer1.addExpense(new Expense("expense-1-1", "Restaurant", new BigDecimal("45.50")));
        payer1.addExpense(new Expense("expense-1-2", "Cinéma", new BigDecimal("12.00")));
        payer1.addExpense(new Expense("expense-1-3", "Essence", new BigDecimal("60.75")));

        Payer payer2 = new Payer("payer-2", "Bob", new ArrayList<>());
        payer2.addExpense(new Expense("expense-2-1", "Hôtel", new BigDecimal("120.00")));
        payer2.addExpense(new Expense("expense-2-2", "Supermarché", new BigDecimal("35.80")));
        payer2.addExpense(new Expense("expense-2-3", "Pharmacie", new BigDecimal("18.30")));

        Payer payer3 = new Payer("payer-3", "Charlie", new ArrayList<>());
        payer3.addExpense(new Expense("expense-3-1", "Train", new BigDecimal("85.00")));
        payer3.addExpense(new Expense("expense-3-2", "Café", new BigDecimal("8.50")));
        payer3.addExpense(new Expense("expense-3-3", "Musée", new BigDecimal("15.00")));

        Room room = new Room(roomId, "fake-room-name", new ArrayList<>());
        room.addPayer(payer1);
        room.addPayer(payer2);
        room.addPayer(payer3);
        return room;
    }

    private static @NonNull FetchRoomResponse createExpectedRoom(String roomId) {
        FetchPayerResponse payer1 = new FetchPayerResponse("payer-1", "Alice", new ArrayList<>());
        payer1.expenses().add(new FetchExpenseResponse("expense-1-1", "Restaurant", new BigDecimal("45.50")));
        payer1.expenses().add(new FetchExpenseResponse("expense-1-2", "Cinéma", new BigDecimal("12.00")));
        payer1.expenses().add(new FetchExpenseResponse("expense-1-3", "Essence", new BigDecimal("60.75")));

        FetchPayerResponse payer2 = new FetchPayerResponse("payer-2", "Bob", new ArrayList<>());
        payer2.expenses().add(new FetchExpenseResponse("expense-2-1", "Hôtel", new BigDecimal("120.00")));
        payer2.expenses().add(new FetchExpenseResponse("expense-2-2", "Supermarché", new BigDecimal("35.80")));
        payer2.expenses().add(new FetchExpenseResponse("expense-2-3", "Pharmacie", new BigDecimal("18.30")));

        FetchPayerResponse payer3 = new FetchPayerResponse("payer-3", "Charlie", new ArrayList<>());
        payer3.expenses().add(new FetchExpenseResponse("expense-3-1", "Train", new BigDecimal("85.00")));
        payer3.expenses().add(new FetchExpenseResponse("expense-3-2", "Café", new BigDecimal("8.50")));
        payer3.expenses().add(new FetchExpenseResponse("expense-3-3", "Musée", new BigDecimal("15.00")));

        FetchRoomResponse room = new FetchRoomResponse(roomId, "fake-room-name", new ArrayList<>());
        room.payers().add(payer1);
        room.payers().add(payer2);
        room.payers().add(payer3);
        return room;
    }

    private static @NonNull Room createRoomWithPayer(String payerId) {
        Payer payer = new Payer(payerId, "fake-payer-name", new ArrayList<>());
        Room room = new Room("fake-room-id", "fake-room-name", new ArrayList<>());
        room.addPayer(payer);
        return room;
    }

    private static @NonNull Room createRoomWithExpense(String expenseId) {
        Expense expense = new Expense(expenseId, "fake-expense-description", new BigDecimal("99.99"));
        Payer payer = new Payer("fake-payer-id", "fake-payer-name", new ArrayList<>());
        payer.addExpense(expense);
        Room room = new Room("fake-room-id", "fake-room-name", new ArrayList<>());
        room.addPayer(payer);
        return room;
    }
}
