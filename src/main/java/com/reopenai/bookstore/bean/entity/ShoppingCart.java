package com.reopenai.bookstore.bean.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Created by Allen Huang
 */
@Data
@Table
public class ShoppingCart {

    @Id
    private Long id;

    /**
     * The ID of the user who owns this shopping cart
     */
    private Long userId;

    /**
     * The ID of the book in the shopping cart
     */
    private Long bookId;

    /**
     * The quantity of the book in the cart
     */
    private Integer quantity;

    /**
     * Timestamp when the cart entry was created
     */
    private LocalDateTime createdTime;

    /**
     * Timestamp when the cart entry was last updated
     */
    private LocalDateTime updatedTime;

}
