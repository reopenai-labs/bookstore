package com.reopenai.bookstore.bean.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class AddBookCategoryRequest {

    @NotBlank(message = "name is required")
    @Length(max = 255, message = "The length of name exceeds the limit")
    @Schema(description = "category name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    private String name;

}
