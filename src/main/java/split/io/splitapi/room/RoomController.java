package split.io.splitapi.room;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import split.io.splitapi.room.models.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRoomResponse create(@Valid @RequestBody CreateRoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FetchRoomResponse fetch(@PathVariable @NotBlank String id) {
        return roomService.fetch(id);
    }

    @PostMapping("/payers")
    @ResponseStatus(HttpStatus.CREATED)
    public AddPayerResponse addPayer(@Valid @RequestBody AddPayerRequest request ) {
        return roomService.addPayer(request);
    }

    @PostMapping("/payers/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddExpenseResponse addExpense(@Valid @RequestBody AddExpenseRequest request ) {
        return roomService.addExpense(request);
    }

    @DeleteMapping("payers/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable @NotBlank String id) {
        roomService.deleteExpense(id);
    }
}
