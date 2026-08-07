package split.io.splitapi.gohan.shopping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoppingConfig {

    @Bean
    public ShoppingMapper shoppingMapper() {
        return new ShoppingMapper();
    }

    @Bean
    public ShoppingFacade shoppingFacade(ShoppingService shoppingService, ShoppingMapper shoppingMapper) {
        return new ShoppingFacade(shoppingService, shoppingMapper);
    }

    @Bean
    public ShoppingService shoppingService(ShoppingPort shoppingPort) {
        return new ShoppingService(shoppingPort);
    }
}
