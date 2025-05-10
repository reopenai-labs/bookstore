package com.reopenai.bookstore.bean.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Book Info
 * <p>
 * Created by Allen Huang
 */
@Data
@Table
public class BookInfo {

    /**
     * book id
     */
    @Id
    private Long id;

    /**
     * book category id
     */
    private Long categoryId;

    /**
     * book title. maxLength = 255
     */
    private String title;

    /**
     * author name. maxLength = 255
     */
    private String author;

    /**
     * price.
     */
    private BigDecimal price;

    /**
     * Creator's ID
     */
    private Long createdBy;

    /**
     * created time
     */
    private LocalDateTime createdTime;

    /**
     * ID of the person who last modified the
     */
    private Long updatedBy;

    /**
     * Last modified time
     */
    private LocalDateTime updatedTime;

}
