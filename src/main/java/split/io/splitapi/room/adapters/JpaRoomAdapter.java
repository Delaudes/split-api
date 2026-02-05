package split.io.splitapi.room.adapters;

import org.springframework.stereotype.Component;
import split.io.splitapi.room.RoomGateway;
import split.io.splitapi.room.dao.ExpenseRepository;
import split.io.splitapi.room.dao.PayerRepository;
import split.io.splitapi.room.dao.RoomRepository;
import split.io.splitapi.room.models.Expense;
import split.io.splitapi.room.models.Payer;
import split.io.splitapi.room.models.Room;
import split.io.splitapi.room.models.entities.ExpenseEntity;
import split.io.splitapi.room.models.entities.PayerEntity;
import split.io.splitapi.room.models.entities.RoomEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class JpaRoomAdapter implements RoomGateway {

    private final RoomRepository roomEntityRepository;
    private final PayerRepository payerEntityRepository;
    private final ExpenseRepository expenseEntityRepository;

    public JpaRoomAdapter(
            RoomRepository roomEntityRepository,
            PayerRepository payerEntityRepository,
            ExpenseRepository expenseEntityRepository
    ) {
        this.roomEntityRepository = roomEntityRepository;
        this.payerEntityRepository = payerEntityRepository;
        this.expenseEntityRepository = expenseEntityRepository;
    }

    @Override
    public void create(Room room) {
        RoomEntity roomEntity = new RoomEntity(room.id(), room.name());
        roomEntityRepository.save(roomEntity);
    }

    @Override
    public Room fetch(String id) {
        RoomEntity roomEntity = roomEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return mapToRoom(roomEntity);
    }

    @Override
    public void addPayer(String roomId, Payer payer) {
        PayerEntity payerEntity = new PayerEntity(payer.id(), payer.name(), roomId);
        payerEntityRepository.save(payerEntity);
    }

    @Override
    public void addExpense(String payerId, Expense expense) {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                expense.id(),
                expense.description(),
                expense.amount(),
                payerId
        );
        expenseEntityRepository.save(expenseEntity);
    }

    @Override
    public void deleteExpense(String id) {
        expenseEntityRepository.deleteById(id);
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
        return new Expense(
                expenseEntity.getId(),
                expenseEntity.getDescription(),
                expenseEntity.getAmount()
        );
    }
}
