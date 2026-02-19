package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "expenses")
public class ExpenseEntity {

    @Id
    private String id;

    private String description;

    private BigDecimal amount;

    @Column(name = "payer_id")
    private String payerId;

    private boolean archived = false;

    public ExpenseEntity() {
    }

    public ExpenseEntity(String id, String description, BigDecimal amount, String payerId, boolean archived) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
        this.archived = archived;
    }
}
