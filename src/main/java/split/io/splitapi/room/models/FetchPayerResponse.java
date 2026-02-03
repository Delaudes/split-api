package split.io.splitapi.room.models;

import java.util.List;

public record FetchPayerResponse(String id, String name, List<FetchExpenseResponse> expenses) {
}
