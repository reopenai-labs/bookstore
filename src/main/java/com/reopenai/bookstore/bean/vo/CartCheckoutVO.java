package com.reopenai.bookstore.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen Huang
 */
@Data
public class CartCheckoutVO {

    @Schema(description = "List of items in the shopping cart")
    private List<SimpleCartItemVO> items;

    @Schema(description = "Total Price")
    private BigDecimal totalPrice;

}
