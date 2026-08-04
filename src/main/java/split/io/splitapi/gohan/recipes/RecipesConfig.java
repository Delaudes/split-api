package split.io.splitapi.gohan.recipes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipesConfig {

    @Bean
    public RecipesMapper recipesMapper() {
        return new RecipesMapper();
    }

    @Bean
    public RecipesFacade recipesFacade(RecipesService recipesService, RecipesMapper recipesMapper) {
        return new RecipesFacade(recipesService, recipesMapper);
    }

    @Bean
    public RecipesService recipesService(RecipesPort recipesPort) {
        return new RecipesService(recipesPort);
    }
}
