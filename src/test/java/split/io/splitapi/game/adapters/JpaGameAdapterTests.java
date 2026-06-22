package split.io.splitapi.game.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.game.dao.FakeGameRepository;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.entities.GameEntity;

import static org.junit.jupiter.api.Assertions.*;

class JpaGameAdapterTests {

    private JpaGameAdapter adapter;
    private FakeGameRepository fakeGameRepository;

    String gameId = "fake-game-id";
    String playerId = "fake-player-id";
    String opponentId = "fake-opponent-id";

    @BeforeEach
    void setUp() {
        fakeGameRepository = new FakeGameRepository();
        adapter = new JpaGameAdapter(fakeGameRepository);
    }

    @Test
    void shouldCreateGame() {
        // Given
        Game game = new Game(gameId, playerId, null);

        // When
        adapter.create(game);

        // Then
        assertEquals(game.id(), fakeGameRepository.savedGame.getGameId());
        assertEquals(game.playerId(), fakeGameRepository.savedGame.getPlayerId());
        assertNull(fakeGameRepository.savedGame.getOpponentId());
    }

    @Test
    void shouldFetchGame() {
        // Given
        fakeGameRepository.gameToReturn = new GameEntity(gameId, playerId);

        // When
        Game result = adapter.fetchGame(gameId);

        // Then
        assertEquals(gameId, fakeGameRepository.findByIdParam);
        assertEquals(gameId, result.id());
        assertEquals(playerId, result.playerId());
        assertNull(result.opponentId());
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        // Given
        fakeGameRepository.gameToReturn = null;

        // When & Then
        assertThrows(RuntimeException.class, () -> adapter.fetchGame("unknown-id"));
    }

    @Test
    void shouldThrowExceptionWhenGameNotFoundOnSaveOpponentId() {
        // Given
        fakeGameRepository.gameToReturn = null;

        // When & Then
        assertThrows(RuntimeException.class, () -> adapter.saveOpponentId("unknown-id", opponentId));
    }

    @Test
    void shouldSaveOpponentId() {
        // Given
        fakeGameRepository.gameToReturn = new GameEntity(gameId, playerId);

        // When
        adapter.saveOpponentId(gameId, opponentId);

        // Then
        assertEquals(gameId, fakeGameRepository.findByIdParam);
        assertEquals(opponentId, fakeGameRepository.savedGame.getOpponentId());
    }
}
