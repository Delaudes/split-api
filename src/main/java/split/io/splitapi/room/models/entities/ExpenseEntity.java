package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "expense_id", referencedColumnName = "id")
    private List<ExcludedPayerEntity> excludedPayers = new ArrayList<>();

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
