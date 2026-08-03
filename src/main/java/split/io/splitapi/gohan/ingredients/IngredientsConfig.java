package split.io.splitapi.gohan.ingredients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngredientsConfig {

    @Bean
    public IngredientsMapper ingredientsMapper() {
        return new IngredientsMapper();
    }

    @Bean
    public IngredientsFacade ingredientsFacade(IngredientsService ingredientsService, IngredientsMapper ingredientsMapper) {
        return new IngredientsFacade(ingredientsService, ingredientsMapper);
    }

    @Bean
    public IngredientsService ingredientsService(IngredientsPort ingredientsPort) {
        return new IngredientsService(ingredientsPort);
    }
}
