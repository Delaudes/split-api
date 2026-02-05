package split.io.splitapi.room.models.outputs;

import java.util.List;

public record FetchRoomResponse(String id, String name, List<FetchPayerResponse> payers) {
}

