package org.nikolait.assigment.ewallet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nikolait.assigment.ewallet.json.RawBigDecimalDeserializer;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.validation.WholeNumber;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateRequest {

    @NotNull
    private UUID walletId;

    @NotNull
    private OperationType operationType;

    @NotNull
    @Positive
    @WholeNumber
    @JsonDeserialize(using = RawBigDecimalDeserializer.class)
    private BigDecimal amount;

}
