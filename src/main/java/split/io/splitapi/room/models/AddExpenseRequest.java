package split.io.splitapi.room.models;

import java.math.BigDecimal;

public record AddExpenseRequest(String payerId, String expenseDescription, BigDecimal expenseAmount) {}
