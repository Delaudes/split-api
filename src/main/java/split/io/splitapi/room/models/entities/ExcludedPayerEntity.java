package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(ExcludedPayerId.class)
@Table(name = "excluded_payers")
public class ExcludedPayerEntity {

    @Id
    @Column(name = "expense_id")
    private String expenseId;

    @Id
    @Column(name = "payer_id")
    private String payerId;


    public ExcludedPayerEntity() {
    }

    public ExcludedPayerEntity(String expenseId, String payerId) {
        this.expenseId = expenseId;
        this.payerId = payerId;
    }
}
