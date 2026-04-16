package split.io.splitapi.room.adapters;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.room.dao.FakeExcludedPayerRepository;
import split.io.splitapi.room.dao.FakeExpenseRepository;
import split.io.splitapi.room.dao.FakePayerRepository;
import split.io.splitapi.room.dao.FakeRoomRepository;
import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;
import split.io.splitapi.room.models.entities.ExcludedPayerEntity;
import split.io.splitapi.room.models.entities.ExpenseEntity;
import split.io.splitapi.room.models.entities.PayerEntity;
import split.io.splitapi.room.models.entities.RoomEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaRoomAdapterTests {

    private JpaRoomAdapter adapter;
    private FakeRoomRepository fakeRoomRepository;
    private FakePayerRepository fakePayerRepository;
    private FakeExpenseRepository fakeExpenseRepository;
    private FakeExcludedPayerRepository fakeExcludedPayerRepository;

    String roomId = "fake-room-id";
    String payerId = "fake-payer-id";
    String expenseId = "fake-expense-id";

    @BeforeEach
    void setUp() {
        fakeRoomRepository = new FakeRoomRepository();
        fakePayerRepository = new FakePayerRepository();
        fakeExpenseRepository = new FakeExpenseRepository();
        fakeExcludedPayerRepository = new FakeExcludedPayerRepository();
        adapter = new JpaRoomAdapter(fakeRoomRepository, fakePayerRepository, fakeExpenseRepository, fakeExcludedPayerRepository);
    }

    @Test
    void shouldCreateRoom() {
        // Given
        Room room = new Room(roomId, "fake-room-name", new ArrayList<>());

        // When
        adapter.create(room);

        // Then
        assertEquals(room.id(), fakeRoomRepository.savedRoom.getId());
        assertEquals(room.name(), fakeRoomRepository.savedRoom.getName());
    }

    @Test
    void shouldFetchRoom() {
        // Given
        RoomEntity roomEntity = createRoomEntity(roomId);
        fakeRoomRepository.roomToReturn = roomEntity;

        // When
        Room result = adapter.fetch(roomId);

        // Then
        assertEquals(roomId, fakeRoomRepository.findByIdParam);
        assertRoomMatchesEntity(result, roomEntity);
    }

    @Test
    void shouldThrowExceptionWhenRoomNotFound() {
        // Given
        fakeRoomRepository.roomToReturn = null;

        // When & Then
        assertThrows(RuntimeException.class, () -> adapter.fetch("unknown-room"));
    }

    @Test
    void shouldAddPayer() {
        // Given
        Payer payer = new Payer(payerId, "fake-payer-name", new ArrayList<>());

        // When
        adapter.addPayer(roomId, payer);

        // Then
        assertEquals(payer.id(), fakePayerRepository.savedPayer.getId());
        assertEquals(payer.name(), fakePayerRepository.savedPayer.getName());
        assertEquals(roomId, fakePayerRepository.savedPayer.getRoomId());
    }

    @Test
    void shouldAddExpense() {
        // Given
        Expense expense = new Expense(expenseId, "fake-expense-description", new BigDecimal("150.00"), false, new ArrayList<>());

        // When
        adapter.addExpense(payerId, expense);

        // Then
        assertEquals(expense.id(), fakeExpenseRepository.savedExpense.getId());
        assertEquals(expense.description(), fakeExpenseRepository.savedExpense.getDescription());
        assertEquals(expense.amount(), fakeExpenseRepository.savedExpense.getAmount());
        assertEquals(payerId, fakeExpenseRepository.savedExpense.getPayerId());
    }

    @Test
    void shouldDeleteExpense() {
        // When
        adapter.deleteExpense(expenseId);

        // Then
        assertEquals(expenseId, fakeExpenseRepository.deletedExpenseId);
    }

    @Test
    void shouldDeleteAllExpenses() {
        // When
        adapter.deleteAllExpenses(roomId);

        // Then
        assertEquals(roomId, fakeExpenseRepository.deletedByRoomId);
    }

    @Test
    void shouldEditRoomName() {
        // Given
        String newName = "new-room-name";
        fakeRoomRepository.roomToReturn = createRoomEntity(roomId);

        // When
        adapter.editRoomName(roomId, newName);

        // Then
        assertEquals(roomId, fakeRoomRepository.findByIdParam);
        assertEquals(newName, fakeRoomRepository.savedRoom.getName());
    }

    @Test
    void shouldEditPayerName() {
        // Given
        String newName = "new-payer-name";
        fakePayerRepository.payerToReturn = new PayerEntity(payerId, "payer-name", roomId);

        // When
        adapter.editPayerName(payerId, newName);

        // Then
        assertEquals(payerId, fakePayerRepository.findByIdParam);
        assertEquals(newName, fakePayerRepository.savedPayer.getName());
    }

    @Test
    void shouldDeletePayer() {
        // When
        adapter.deletePayer(payerId);

        // Then
        assertEquals(payerId, fakePayerRepository.deletedPayerId);
    }

    @Test
    void shouldDeleteRoom() {
        // When
        adapter.deleteRoom(roomId);

        // Then
        assertEquals(roomId, fakeRoomRepository.deletedRoomId);
    }

    @Test
    void shouldArchiveAllExpenses() {
        // When
        adapter.archiveAllExpenses(roomId);

        // Then
        assertEquals(roomId, fakeExpenseRepository.archivedByRoomId);
    }

    @Test
    void shouldAddExpensePayer() {
        // When
        adapter.excludeExpensePayer(expenseId, payerId);

        // Then
        assertEquals(expenseId, fakeExcludedPayerRepository.deletedExcludedPayer.getExpenseId());
        assertEquals(payerId, fakeExcludedPayerRepository.deletedExcludedPayer.getPayerId());
    }

    @Test
    void shouldDeleteExpensePayer() {
        // When
        adapter.includeExpensePayer(expenseId, payerId);

        // Then
        assertEquals(expenseId, fakeExcludedPayerRepository.savedExcludedPayer.getExpenseId());
        assertEquals(payerId, fakeExcludedPayerRepository.savedExcludedPayer.getPayerId());
    }

    private static @NonNull RoomEntity createRoomEntity(String roomId) {
        RoomEntity roomEntity = new RoomEntity(roomId, "Weekend");

        PayerEntity payer1 = new PayerEntity("payer-1", "Alice", roomId);
        ExpenseEntity expense1 = new ExpenseEntity("expense-1", "Restaurant", new BigDecimal("50.00"), "payer-1", false);
        ExpenseEntity expense2 = new ExpenseEntity("expense-2", "Cinéma", new BigDecimal("30.00"), "payer-1", false);
        ExcludedPayerEntity excludedPayer = new ExcludedPayerEntity("expense-2", "payer-1");
        expense2.setExcludedPayers(List.of(excludedPayer));
        payer1.setExpenses(List.of(expense1, expense2));

        PayerEntity payer2 = new PayerEntity("payer-2", "Bob", roomId);
        ExpenseEntity expense3 = new ExpenseEntity("expense-3", "Essence", new BigDecimal("40.00"), "payer-2", false);
        payer2.setExpenses(List.of(expense3));

        roomEntity.setPayers(List.of(payer1, payer2));

        return roomEntity;
    }

    private static void assertRoomMatchesEntity(Room room, RoomEntity roomEntity) {
        assertEquals(roomEntity.getId(), room.id());
        assertEquals(roomEntity.getName(), room.name());
        assertEquals(roomEntity.getPayers().size(), room.payers().size());

        for (int i = 0; i < roomEntity.getPayers().size(); i++) {
            PayerEntity payerEntity = roomEntity.getPayers().get(i);
            Payer payer = room.payers().get(i);

            assertEquals(payerEntity.getId(), payer.id());
            assertEquals(payerEntity.getName(), payer.name());
            assertEquals(payerEntity.getExpenses().size(), payer.expenses().size());

            for (int j = 0; j < payerEntity.getExpenses().size(); j++) {
                ExpenseEntity expenseEntity = payerEntity.getExpenses().get(j);
                Expense expense = payer.expenses().get(j);

                assertEquals(expenseEntity.getId(), expense.id());
                assertEquals(expenseEntity.getDescription(), expense.description());
                assertEquals(expenseEntity.getAmount(), expense.amount());

                for (int k = 0; k < expenseEntity.getExcludedPayers().size(); k++) {
                    ExcludedPayerEntity excludedPayerEntity = expenseEntity.getExcludedPayers().get(k);

                    assertTrue(expense.excludedPayersId().contains(excludedPayerEntity.getPayerId()));
                }
            }
        }
    }
}
