package com.reopenai.bookstore.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Allen Huang
 */
@Data
public class SimpleCartItemVO {

    @Schema(description = "book id")
    private Long bookId;

    @Schema(description = "book title")
    private String title;

    @Schema(description = "book author")
    private String author;

    @Schema(description = "quantity")
    private Integer quantity;

    @Schema(description = "price")
    private BigDecimal price;

    @Schema(description = "The total price of this book")
    private BigDecimal totalPrice;

}
