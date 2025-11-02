package com.fraud.transaction.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlagTransactionRequest {

    @NotBlank(message = "Comment is required to flag a transaction.")
    private String comment;
}
