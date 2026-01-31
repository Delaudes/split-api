package split.io.splitapi.uuid;

import java.util.UUID;

public class JavaUuidGenerator implements UuidGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
