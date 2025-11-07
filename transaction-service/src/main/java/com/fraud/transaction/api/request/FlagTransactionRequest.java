package com.fraud.transaction.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for flagging a transaction as potentially fraudulent.
 * This is used by the controller when an admin or system flags a transaction.
 */
@Data
public class FlagTransactionRequest {

    @NotBlank(message = "Comment is required to flag a transaction.")
    private String comment;
}
