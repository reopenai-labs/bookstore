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
public class BookCategory {

    @Id
    private Long id;

    /**
     * category name
     */
    private String name;

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
