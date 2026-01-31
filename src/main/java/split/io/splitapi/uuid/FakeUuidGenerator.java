package split.io.splitapi.uuid;

public class FakeUuidGenerator implements UuidGenerator {

     String uuid = "fake-uuid-123";

    @Override
    public String generate() {
        return uuid;
    }
}
