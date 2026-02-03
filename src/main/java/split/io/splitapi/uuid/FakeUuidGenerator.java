package split.io.splitapi.uuid;

public class FakeUuidGenerator implements UuidGenerator {

     String uuid = "fake-uuid";

    @Override
    public String generate() {
        return uuid;
    }
}
