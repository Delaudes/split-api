package split.io.splitapi.game.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    private String gameId;

    private String playerId;

    @Setter
    private String opponentId;

    public GameEntity() {
    }

    public GameEntity(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
