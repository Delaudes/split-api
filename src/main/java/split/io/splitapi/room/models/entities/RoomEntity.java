package split.io.splitapi.room.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    private String id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<PayerEntity> payers = new ArrayList<>();

    public RoomEntity() {
    }

    public RoomEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
