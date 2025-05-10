package com.reopenai.bookstore.bean.vo;

import com.reopenai.bookstore.bean.entity.BookCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created by Allen Huang
 */
@Data
public class BookCategoryVO {

    @Schema(description = "category id")
    private Long id;

    @Schema(description = "category name")
    private String name;

    public static BookCategoryVO from(BookCategory entity) {
        BookCategoryVO detail = new BookCategoryVO();
        detail.setId(entity.getId());
        detail.setName(entity.getName());
        return detail;
    }

}
