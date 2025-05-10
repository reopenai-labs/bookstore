package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Created by Allen Huang
 */
@Data
public class QueryBookCategoryRequest {

    @Min(value = 1, message = "The limit cannot less than 1")
    @Max(value = 1024, message = "The limit cannot be greater than 1024")
    @Schema(description = "limit", defaultValue = "50", minimum = "1", maximum = "1024")
    private int limit;

    @Schema(description = "cursor")
    @Min(value = 1, message = "The cursor cannot less than 1")
    private Long cursor;

    @Schema(description = "category id")
    @Min(value = 1, message = "The id cannot less than 1")
    private Long id;

}
