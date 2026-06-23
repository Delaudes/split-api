package split.io.splitapi.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.game.adapters.FakeGameAdapter;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.ActionType;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.inputs.PlayRequest;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.FetchGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;
import split.io.splitapi.uuid.FakeUuidGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameFacadeTests {

    private static final String GAME_ID = "fake-game-id";
    private static final String PLAYER_ID = "fake-player-id";
    private static final String OPPONENT_ID = "fake-opponent-id";

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
        Game expectedGame = new Game(fakeUuidGenerator.uuid, fakeUuidGenerator.uuid, null, new ArrayList<>());

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
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, new ArrayList<>());

        // When & Then
        assertThrows(RuntimeException.class, () -> gameFacade.join(GAME_ID));
    }

    @Test
    void shouldRejectPlayIfPlayerNotInGame() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, new ArrayList<>());
        PlayRequest request = new PlayRequest("unknown-player-id", 2, 3);

        // When & Then
        assertThrows(RuntimeException.class, () -> gameFacade.play(GAME_ID, request));
    }

    @Test
    void shouldPlay() {
        // Given — no actions yet, so round 1 and PLACE are determined from game state
        PlayRequest request = new PlayRequest(PLAYER_ID, 2, 3);
        Action expectedAction = new Action(fakeUuidGenerator.uuid, GAME_ID, PLAYER_ID, 2, 3, ActionType.PLACE, 1);

        // When
        gameFacade.play(GAME_ID, request);

        // Then
        assertEquals(expectedAction, fakeGameAdapter.savedAction);
    }

    @Test
    void shouldDetermineRoundAndTypeFromGameState() {
        // Given — player has 3 PLACE in round 1, so next action is PREDICT in round 1
        List<Action> existingActions = new ArrayList<>(List.of(
                new Action("a1", GAME_ID, PLAYER_ID, 0, 0, ActionType.PLACE, 1),
                new Action("a2", GAME_ID, PLAYER_ID, 1, 0, ActionType.PLACE, 1),
                new Action("a3", GAME_ID, PLAYER_ID, 2, 0, ActionType.PLACE, 1)
        ));
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, existingActions);
        PlayRequest request = new PlayRequest(PLAYER_ID, 3, 0);
        Action expectedAction = new Action(fakeUuidGenerator.uuid, GAME_ID, PLAYER_ID, 3, 0, ActionType.PREDICT, 1);

        // When
        gameFacade.play(GAME_ID, request);

        // Then
        assertEquals(expectedAction, fakeGameAdapter.savedAction);
    }

    @Test
    void shouldRejectFetchIfPlayerNotInGame() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, new ArrayList<>());

        // When & Then
        assertThrows(RuntimeException.class, () -> gameFacade.fetchGameForPlayer(GAME_ID, "unknown-player-id"));
    }

    @Test
    void shouldReturnEmptyListsWhenNoActions() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, new ArrayList<>());

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(0, response.playerActions().size());
        assertEquals(0, response.opponentActions().size());
    }

    @Test
    void shouldReturnActionsForCompletedRound() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, completeRound(1));

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(5, response.playerActions().size());
        assertEquals(5, response.opponentActions().size());
    }

    @Test
    void shouldReturnActionsForMultipleCompletedRounds() {
        // Given
        List<Action> actions = new ArrayList<>();
        actions.addAll(completeRound(1));
        actions.addAll(completeRound(2));
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, actions);

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(10, response.playerActions().size());
        assertEquals(10, response.opponentActions().size());
    }

    @Test
    void shouldStopAtFirstIncompleteRound() {
        // Given
        List<Action> actions = new ArrayList<>();
        actions.addAll(completeRound(1));
        actions.addAll(incompleteRound(2));
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, actions);

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(5, response.playerActions().size());
        assertEquals(5, response.opponentActions().size());
    }

    @Test
    void shouldReturnEmptyListsWhenFirstRoundIsIncomplete() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, incompleteRound(1));

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(0, response.playerActions().size());
        assertEquals(0, response.opponentActions().size());
    }

    @Test
    void shouldReturnEmptyListsWhenRoundOneIsMissing() {
        // Given — round 2 is complete but round 1 has no actions
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, completeRound(2));

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then — round 1 is empty so we stop immediately
        assertEquals(0, response.playerActions().size());
        assertEquals(0, response.opponentActions().size());
    }

    @Test
    void shouldSplitPlayerAndOpponentActionsCorrectly() {
        // Given
        fakeGameAdapter.game = new Game(GAME_ID, PLAYER_ID, OPPONENT_ID, completeRound(1));

        // When
        FetchGameResponse response = gameFacade.fetchGameForPlayer(GAME_ID, PLAYER_ID);

        // Then
        assertEquals(3, response.playerActions().stream().filter(a -> a.type() == ActionType.PLACE).count());
        assertEquals(2, response.playerActions().stream().filter(a -> a.type() == ActionType.PREDICT).count());
        assertEquals(3, response.opponentActions().stream().filter(a -> a.type() == ActionType.PLACE).count());
        assertEquals(2, response.opponentActions().stream().filter(a -> a.type() == ActionType.PREDICT).count());
    }

    private static List<Action> completeRound(int round) {
        return List.of(
                new Action("p-pl-1-" + round, GAME_ID, PLAYER_ID, 0, 0, ActionType.PLACE, round),
                new Action("p-pl-2-" + round, GAME_ID, PLAYER_ID, 1, 0, ActionType.PLACE, round),
                new Action("p-pl-3-" + round, GAME_ID, PLAYER_ID, 2, 0, ActionType.PLACE, round),
                new Action("p-pr-1-" + round, GAME_ID, PLAYER_ID, 3, 0, ActionType.PREDICT, round),
                new Action("p-pr-2-" + round, GAME_ID, PLAYER_ID, 4, 0, ActionType.PREDICT, round),
                new Action("o-pl-1-" + round, GAME_ID, OPPONENT_ID, 0, 1, ActionType.PLACE, round),
                new Action("o-pl-2-" + round, GAME_ID, OPPONENT_ID, 1, 1, ActionType.PLACE, round),
                new Action("o-pl-3-" + round, GAME_ID, OPPONENT_ID, 2, 1, ActionType.PLACE, round),
                new Action("o-pr-1-" + round, GAME_ID, OPPONENT_ID, 3, 1, ActionType.PREDICT, round),
                new Action("o-pr-2-" + round, GAME_ID, OPPONENT_ID, 4, 1, ActionType.PREDICT, round)
        );
    }

    private static List<Action> incompleteRound(int round) {
        return List.of(
                new Action("p-pl-1-" + round, GAME_ID, PLAYER_ID, 0, 0, ActionType.PLACE, round),
                new Action("p-pl-2-" + round, GAME_ID, PLAYER_ID, 1, 0, ActionType.PLACE, round)
        );
    }
}
