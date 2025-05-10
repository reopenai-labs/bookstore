package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Allen Huang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookCategoryRequest {

    @NotNull(message = "id is required")
    @Min(value = 1, message = "invalid id")
    @Schema(description = "category id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "name is required")
    @Length(max = 255, message = "The length of name exceeds the limit")
    @Schema(description = "category name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    private String name;

}
