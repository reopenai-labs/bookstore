package com.reopenai.bookstore.bean.vo;

import com.reopenai.bookstore.bean.entity.BookInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Allen Huang
 */
@Data
public class BookDetailVO {

    @Schema(description = "book id")
    private Long id;

    @Schema(description = "book title")
    private String title;

    @Schema(description = "book category id")
    private Long categoryId;

    @Schema(description = "book category name")
    private String categoryName;

    @Schema(description = "book author")
    private String author;

    @Schema(description = "book price")
    private BigDecimal price;

    public static BookDetailVO from(BookInfo bookInfo) {
        BookDetailVO bookDetailVO = new BookDetailVO();
        bookDetailVO.setId(bookInfo.getId());
        bookDetailVO.setTitle(bookInfo.getTitle());
        bookDetailVO.setPrice(bookInfo.getPrice());
        bookDetailVO.setAuthor(bookInfo.getAuthor());
        bookDetailVO.setCategoryId(bookInfo.getCategoryId());
        return bookDetailVO;
    }

}
