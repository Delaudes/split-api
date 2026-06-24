package split.io.splitapi.game.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.game.dao.FakeActionRepository;
import split.io.splitapi.game.dao.FakeGameRepository;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.ActionType;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.entities.ActionEntity;
import split.io.splitapi.game.models.entities.GameEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaGameAdapterTests {

    private JpaGameAdapter adapter;
    private FakeGameRepository fakeGameRepository;
    private FakeActionRepository fakeActionRepository;

    String gameId = "fake-game-id";
    String playerId = "fake-player-id";
    String opponentId = "fake-opponent-id";

    @BeforeEach
    void setUp() {
        fakeGameRepository = new FakeGameRepository();
        fakeActionRepository = new FakeActionRepository();
        adapter = new JpaGameAdapter(fakeGameRepository, fakeActionRepository);
    }

    @Test
    void shouldCreateGame() {
        // Given
        Game game = new Game(gameId, playerId, null, new ArrayList<>());

        // When
        adapter.create(game);

        // Then
        assertEquals(game.id(), fakeGameRepository.savedGame.getId());
        assertEquals(game.playerId(), fakeGameRepository.savedGame.getPlayerId());
        assertNull(fakeGameRepository.savedGame.getOpponentId());
    }

    @Test
    void shouldFetchGame() {
        // Given
        GameEntity entity = new GameEntity(gameId, playerId);
        ActionEntity actionEntity = new ActionEntity("action-1", gameId, playerId, 2, 3, ActionType.PLACE, 1);
        entity.setActions(List.of(actionEntity));
        fakeGameRepository.gameToReturn = entity;

        // When
        Game result = adapter.fetchGame(gameId);

        // Then
        assertEquals(gameId, fakeGameRepository.findByIdParam);
        assertEquals(gameId, result.id());
        assertEquals(playerId, result.playerId());
        assertNull(result.opponentId());
        assertEquals(1, result.actions().size());
        Action action = result.actions().getFirst();
        assertEquals("action-1", action.id());
        assertEquals(2, action.x());
        assertEquals(3, action.y());
        assertEquals(ActionType.PLACE, action.type());
        assertEquals(1, action.round());
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

    @Test
    void shouldAddAction() {
        // Given
        Action action = new Action("action-1", gameId, playerId, 2, 3, ActionType.PREDICT, 2);

        // When
        adapter.addAction(action);

        // Then
        assertEquals(action.id(), fakeActionRepository.savedAction.getId());
        assertEquals(action.gameId(), fakeActionRepository.savedAction.getGameId());
        assertEquals(action.playerId(), fakeActionRepository.savedAction.getPlayerId());
        assertEquals(action.x(), fakeActionRepository.savedAction.getX());
        assertEquals(action.y(), fakeActionRepository.savedAction.getY());
        assertEquals(action.type(), fakeActionRepository.savedAction.getType());
        assertEquals(action.round(), fakeActionRepository.savedAction.getRound());
    }
}
