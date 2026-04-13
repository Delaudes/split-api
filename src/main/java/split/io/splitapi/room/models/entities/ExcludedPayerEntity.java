package split.io.splitapi.room.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "excluded_payers")
public class ExcludedPayerEntity {

    @Column(name = "expense_id")
    private String expenseId;

    @Column(name = "payer_id")
    private String payerId;


    public ExcludedPayerEntity() {
    }

    public ExcludedPayerEntity(String expenseId, String payerId) {
        this.expenseId = expenseId;
        this.payerId = payerId;
    }
}
