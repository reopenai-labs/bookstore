package com.reopenai.bookstore.bean.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Allen Huang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequest {

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity cannot less than 1")
    @Schema(description = "quantity", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "1", minimum = "1")
    private Integer quantity = 1;

    @NotNull(message = "bookId is required")
    @Min(value = 1, message = "invalid bookId")
    @Schema(description = "bookId", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long bookId;

    @Hidden
    @JsonIgnore
    private Long userId;

}
