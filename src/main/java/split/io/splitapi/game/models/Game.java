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

    public boolean isInvalidAction(Action action) {
        long count = actions.stream()
                .filter(a -> a.playerId().equals(action.playerId()))
                .filter(a -> a.round() == action.round())
                .filter(a -> a.type() == action.type())
                .count();
        return switch (action.type()) {
            case PLACE -> count >= 3;
            case PREDICT -> count >= 2;
        };
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
