package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * Created by Allen Huang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest {

    @Min(value = 1, message = "invalid categoryId")
    @NotNull(message = "categoryId is required")
    @Schema(description = "book categoryId", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long categoryId;

    @NotBlank(message = "title is required")
    @Length(max = 255, message = "The length of title exceeds the limit")
    @Schema(description = "book title", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    private String title;

    @NotBlank(message = "author is required")
    @Length(max = 255, message = "The length of author exceeds the limit")
    @Schema(description = "author", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    private String author;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0", message = "price cannot less than 0")
    @Digits(integer = 15, fraction = 4, message = "The integer part of the price can have a maximum of 15 digits, and the decimal part is limited to 4 digits")
    @Schema(description = "price", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal price;

}
