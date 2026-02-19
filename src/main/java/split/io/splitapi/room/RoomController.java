package split.io.splitapi.room;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import split.io.splitapi.room.models.inputs.*;
import split.io.splitapi.room.models.outputs.AddExpenseResponse;
import split.io.splitapi.room.models.outputs.AddPayerResponse;
import split.io.splitapi.room.models.outputs.CreateRoomResponse;
import split.io.splitapi.room.models.outputs.FetchRoomResponse;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {

    private final RoomFacade roomFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRoomResponse create(@Valid @RequestBody CreateRoomRequest request) {
        return roomFacade.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FetchRoomResponse fetch(@PathVariable @NotBlank String id) {
        return roomFacade.fetch(id);
    }

    @PostMapping("/payers")
    @ResponseStatus(HttpStatus.CREATED)
    public AddPayerResponse addPayer(@Valid @RequestBody AddPayerRequest request ) {
        return roomFacade.addPayer(request);
    }

    @PostMapping("/payers/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddExpenseResponse addExpense(@Valid @RequestBody AddExpenseRequest request ) {
        return roomFacade.addExpense(request);
    }

    @DeleteMapping("/payers/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable @NotBlank String id) {
        roomFacade.deleteExpense(id);
    }

    @DeleteMapping("/{id}/payers/expenses")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllExpenses(@PathVariable @NotBlank String id) {
         roomFacade.deleteAllExpenses(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editRoomName(@PathVariable @NotBlank String id, @Valid @RequestBody EditRoomRequest request) {
        roomFacade.editRoomName(id, request);
    }

    @PutMapping("/payers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editPayerName(@PathVariable @NotBlank String id, @Valid @RequestBody EditPayerRequest request) {
        roomFacade.editPayerName(id, request);
    }

    @DeleteMapping("/payers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayer(@PathVariable @NotBlank String id) {
        roomFacade.deletePayer(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable @NotBlank String id) {
        roomFacade.deleteRoom(id);
    }

    @GetMapping("/{id}/history")
    @ResponseStatus(HttpStatus.OK)
    public FetchRoomResponse fetchHistory(@PathVariable @NotBlank String id) {
        return roomFacade.fetchHistory(id);
    }
}
