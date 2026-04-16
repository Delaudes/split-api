package split.io.splitapi.room.models;

import java.math.BigDecimal;
import java.util.ArrayList;

public record Expense(String id, String description, BigDecimal amount, boolean archived, ArrayList<String> excludedPayersId) {

     public void addExcludedPayer(String payerId) {
         this.excludedPayersId.add(payerId);
     }
}
