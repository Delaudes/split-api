package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "payers")
public class PayerEntity {

    @Id
    private String id;

    @Setter
    private String name;

    @Column(name = "room_id")
    private String roomId;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private List<ExpenseEntity> expenses = new ArrayList<>();

    public PayerEntity() {
    }

    public PayerEntity(String id, String name, String roomId) {
        this.id = id;
        this.name = name;
        this.roomId = roomId;
    }
}
