package split.io.splitapi.room;

import lombok.Getter;

@Getter
public class Room {

    private final String id;
    private final String name;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
