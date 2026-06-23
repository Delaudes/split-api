package split.io.splitapi.game.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Game(String id, String playerId, String opponentId, List<Action> actions) {

    public boolean hasOpponent() {
        return opponentId != null;
    }

    public boolean isNotPlayer(String requestingPlayerId) {
        return !requestingPlayerId.equals(playerId) && !requestingPlayerId.equals(opponentId);
    }

    public int getNextRound(String playerId) {
        Map<Integer, List<Action>> playerActionsByRound = actions.stream()
                .filter(a -> a.playerId().equals(playerId))
                .collect(Collectors.groupingBy(Action::round));

        for (int round = 1; ; round++) {
            if (isRoundIncomplete(playerActionsByRound.getOrDefault(round, List.of()))) {
                return round;
            }
        }
    }

    public ActionType getNextActionType(String playerId) {
        int currentRound = getNextRound(playerId);
        long placeCount = actions.stream()
                .filter(a -> a.playerId().equals(playerId))
                .filter(a -> a.round() == currentRound)
                .filter(a -> a.type() == ActionType.PLACE)
                .count();
        return placeCount < 3 ? ActionType.PLACE : ActionType.PREDICT;
    }

    public GameView buildPlayerView(String requestingPlayerId) {
        String otherPlayerId = requestingPlayerId.equals(playerId) ? opponentId : playerId;

        Map<Integer, List<Action>> actionsByRound = actions.stream()
                .collect(Collectors.groupingBy(Action::round));

        List<Action> validPlayerActions = new ArrayList<>();
        List<Action> validOpponentActions = new ArrayList<>();

        for (int round = 1; ; round++) {
            List<Action> roundActions = actionsByRound.getOrDefault(round, List.of());

            List<Action> pActions = roundActions.stream()
                    .filter(a -> a.playerId().equals(requestingPlayerId)).toList();
            List<Action> oActions = roundActions.stream()
                    .filter(a -> a.playerId().equals(otherPlayerId)).toList();

            if (isRoundIncomplete(pActions) || isRoundIncomplete(oActions)) {
                break;
            }

            validPlayerActions.addAll(pActions);
            validOpponentActions.addAll(oActions);
        }

        return new GameView(validPlayerActions, validOpponentActions);
    }

    private static boolean isRoundIncomplete(List<Action> actions) {
        long placeCount = actions.stream().filter(a -> a.type() == ActionType.PLACE).count();
        long predictCount = actions.stream().filter(a -> a.type() == ActionType.PREDICT).count();
        return placeCount != 3 || predictCount != 2;
    }
}
