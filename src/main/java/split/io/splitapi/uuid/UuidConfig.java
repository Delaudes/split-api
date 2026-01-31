package split.io.splitapi.uuid;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UuidConfig {

    @Bean
    public UuidGenerator uuidGenerator() {
        return new JavaUuidGenerator();
    }
}