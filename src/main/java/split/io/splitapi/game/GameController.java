package split.io.splitapi.game;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import split.io.splitapi.game.models.inputs.PlayRequest;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Validated
public class GameController {

    private final GameFacade gameFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateGameResponse create() {
        return gameFacade.create();
    }

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.OK)
    public JoinGameResponse join(@PathVariable @NotBlank String id) {
        return gameFacade.join(id);
    }

    @PostMapping("/{id}/play")
    @ResponseStatus(HttpStatus.OK)
    public void play(@PathVariable @NotBlank String id, @Valid @RequestBody PlayRequest request) {
        gameFacade.play(id, request);
    }
}
