package split.io.splitapi.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.game.adapters.FakeGameAdapter;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;
import split.io.splitapi.uuid.FakeUuidGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameFacadeTests {

    private GameFacade gameFacade;
    private FakeGameAdapter fakeGameAdapter;
    private FakeUuidGenerator fakeUuidGenerator;

    @BeforeEach
    void setUp() {
        fakeGameAdapter = new FakeGameAdapter();
        fakeUuidGenerator = new FakeUuidGenerator();
        GameMapper gameMapper = new GameMapper();
        GameService gameService = new GameService(fakeGameAdapter);
        gameFacade = new GameFacade(gameService, gameMapper, fakeUuidGenerator);
    }

    @Test
    void shouldCreateGameWithGeneratedIds() {
        // Given
        Game expectedGame = new Game(fakeUuidGenerator.uuid, fakeUuidGenerator.uuid, null);

        // When
        CreateGameResponse response = gameFacade.create();

        // Then
        assertEquals(fakeUuidGenerator.uuid, response.id());
        assertEquals(fakeUuidGenerator.uuid, response.playerId());
        assertEquals(expectedGame, fakeGameAdapter.game);
    }

    @Test
    void shouldJoinGameWithGeneratedOpponentId() {
        // Given
        String gameId = "fake-game-id";

        // When
        JoinGameResponse response = gameFacade.join(gameId);

        // Then
        assertEquals(gameId, response.id());
        assertEquals(fakeUuidGenerator.uuid, response.playerId());
        assertEquals(gameId, fakeGameAdapter.savedOpponentGameId);
        assertEquals(fakeUuidGenerator.uuid, fakeGameAdapter.savedOpponentId);
    }

    @Test
    void shouldRejectJoinIfOpponentAlreadyExists() {
        // Given
        String gameId = "fake-game-id";
        fakeGameAdapter.game = new Game(gameId, "fake-player-id", "existing-opponent-id");

        // When & Then
        assertThrows(RuntimeException.class, () -> gameFacade.join(gameId));
    }
}
