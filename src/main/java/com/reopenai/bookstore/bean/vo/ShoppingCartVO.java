package com.reopenai.bookstore.bean.vo;

import com.reopenai.bookstore.bean.entity.ShoppingCart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Allen Huang
 */
@Data
public class ShoppingCartVO {

    @Schema(description = "shopping cart entity id")
    private Long id;

    @Schema(description = "book information")
    private SimpleBookInfoVO bookInfo;

    @Schema(description = "quantity")
    private Integer quantity;

    @Schema(description = "created time", type = "integer")
    private LocalDateTime createdTime;

    @Schema(description = "last updated time", type = "integer")
    private LocalDateTime updatedTime;

    public static ShoppingCartVO from(ShoppingCart shoppingCart) {
        ShoppingCartVO entity = new ShoppingCartVO();
        entity.setId(shoppingCart.getId());
        entity.setQuantity(shoppingCart.getQuantity());
        entity.setCreatedTime(shoppingCart.getCreatedTime());
        entity.setUpdatedTime(shoppingCart.getUpdatedTime());
        return entity;
    }

}
