package split.io.splitapi.game.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    private String gameId;

    private String playerId;

    @Setter
    private String opponentId;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private List<ActionEntity> actions = new ArrayList<>();

    public GameEntity() {
    }

    public GameEntity(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
