package split.io.splitapi.room.models.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ExcludedPayerId implements Serializable {
    private String expenseId;
    private String payerId;
}
