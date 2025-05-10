package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Allen Huang
 */
@Data
public class QueryBookRequest {

    @Min(value = 1, message = "The limit cannot less than 1")
    @Max(value = 256, message = "The limit cannot be greater than 256")
    @Schema(description = "limit", defaultValue = "50", minimum = "1", maximum = "256")
    private int limit = 50;

    @Schema(description = "cursor", minimum = "1")
    @Min(value = 1, message = "The cursor cannot less than 1")
    private Long cursor;

    @Min(value = 1, message = "invalid categoryId")
    @Schema(description = "book category id", minimum = "1")
    private Long categoryId;

    @Schema(description = "book id", minimum = "1")
    @Min(value = 1, message = "The id cannot less than 1")
    private Long id;

    @Schema(description = "book title", maxLength = 255)
    @Length(max = 255, message = "The length of title exceeds the limit")
    private String title;

    @Schema(description = "book author", maxLength = 255)
    @Length(max = 255, message = "The length of author exceeds the limit")
    private String author;

}
