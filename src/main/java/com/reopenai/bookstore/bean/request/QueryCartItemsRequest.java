package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Created by Allen Huang
 */
@Data
public class QueryCartItemsRequest {

    @Min(value = 1, message = "The limit cannot less than 1")
    @Max(value = 100, message = "The limit cannot be greater than 100")
    @Schema(description = "records size", minimum = "1", maximum = "100", defaultValue = "20")
    private int limit = 20;

    @Schema(description = "cursor", minimum = "1")
    @Min(value = 1, message = "The cursor cannot less than 1")
    private Long cursor;

    @Hidden
    private Long userId;

}
