package split.io.splitapi.gohan.ingredients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import split.io.splitapi.uuid.UuidGenerator;

@Configuration
public class IngredientsConfig {

    @Bean
    public IngredientsMapper ingredientsMapper() {
        return new IngredientsMapper();
    }

    @Bean
    public IngredientsFacade ingredientsFacade(IngredientsService ingredientsService, IngredientsMapper ingredientsMapper, UuidGenerator uuidGenerator) {
        return new IngredientsFacade(ingredientsService, ingredientsMapper, uuidGenerator);
    }

    @Bean
    public IngredientsService ingredientsService(IngredientsPort ingredientsPort) {
        return new IngredientsService(ingredientsPort);
    }
}
