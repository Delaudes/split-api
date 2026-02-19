package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    private String id;

    @Setter
    private String name;

    @Setter
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<PayerEntity> payers = new ArrayList<>();

    public RoomEntity() {
    }

    public RoomEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
