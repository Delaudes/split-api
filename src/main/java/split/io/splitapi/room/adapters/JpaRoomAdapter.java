package split.io.splitapi.room.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.room.RoomPort;
import split.io.splitapi.room.dao.ExcludedPayerRepository;
import split.io.splitapi.room.dao.ExpenseRepository;
import split.io.splitapi.room.dao.PayerRepository;
import split.io.splitapi.room.dao.RoomRepository;
import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;
import split.io.splitapi.room.models.entities.ExcludedPayerEntity;
import split.io.splitapi.room.models.entities.ExpenseEntity;
import split.io.splitapi.room.models.entities.PayerEntity;
import split.io.splitapi.room.models.entities.RoomEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRoomAdapter implements RoomPort {

    private final RoomRepository roomRepository;
    private final PayerRepository payerRepository;
    private final ExpenseRepository expenseRepository;
    private final ExcludedPayerRepository excludedPayerRepository;

    @Override
    public void create(Room room) {
        RoomEntity roomEntity = new RoomEntity(room.id(), room.name());
        roomRepository.save(roomEntity);
    }

    @Override
    public Room fetch(String id) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return mapToRoom(roomEntity);
    }

    @Override
    public void addPayer(String roomId, Payer payer) {
        PayerEntity payerEntity = new PayerEntity(payer.id(), payer.name(), roomId);
        payerRepository.save(payerEntity);
    }

    @Override
    public void addExpense(String payerId, Expense expense) {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                expense.id(),
                expense.description(),
                expense.amount(),
                payerId, false
        );
        expenseRepository.save(expenseEntity);
    }

    @Override
    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public void deleteAllExpenses(String roomId) {
        expenseRepository.deleteByRoomId(roomId);
    }

    @Override
    public void editRoomName(String id, String name) {
        RoomEntity roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        roomEntity.setName(name);
        roomRepository.save(roomEntity);
    }

    @Override
    public void editPayerName(String id, String name) {
        PayerEntity payerEntity = payerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payer not found with id: " + id));
        payerEntity.setName(name);
        payerRepository.save(payerEntity);
    }

    @Override
    public void deletePayer(String id) {
        payerRepository.deleteById(id);
    }

    @Override
    public void deleteRoom(String id) {
        roomRepository.deleteById(id);
    }

    @Override
    public void archiveAllExpenses(String roomId) {
        expenseRepository.archiveByRoomId(roomId);
    }

    @Override
    public void addExpensePayer(String expenseId, String payerId) {
        ExcludedPayerEntity excludedPayer = new ExcludedPayerEntity(expenseId, payerId);
        excludedPayerRepository.delete(excludedPayer);
    }

    @Override
    public void deleteExpensePayer(String expenseId, String payerId) {
        ExcludedPayerEntity excludedPayer = new ExcludedPayerEntity(expenseId, payerId);
        excludedPayerRepository.save(excludedPayer);
    }

    private Room mapToRoom(RoomEntity roomEntity) {
        ArrayList<Payer> payers = roomEntity.getPayers().stream()
                .map(this::mapToPayer)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Room(roomEntity.getId(), roomEntity.getName(), payers);
    }

    private Payer mapToPayer(PayerEntity payerEntity) {
        ArrayList<Expense> expenses = payerEntity.getExpenses().stream()
                .map(this::mapToExpense)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Payer(payerEntity.getId(), payerEntity.getName(), expenses);
    }

    private Expense mapToExpense(ExpenseEntity expenseEntity) {
        ArrayList<String> excludedPayers = expenseEntity.getExcludedPayers().stream()
                .map(ExcludedPayerEntity::getPayerId)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Expense(
                expenseEntity.getId(),
                expenseEntity.getDescription(),
                expenseEntity.getAmount(), expenseEntity.isArchived(),
                excludedPayers
        );
    }
}
