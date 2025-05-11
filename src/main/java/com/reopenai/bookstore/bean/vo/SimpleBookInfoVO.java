package com.reopenai.bookstore.bean.vo;

import com.reopenai.bookstore.bean.entity.BookInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Allen Huang
 */
@Data
public class SimpleBookInfoVO {

    @Schema(description = "book id")
    private Long id;

    @Schema(description = "book title")
    private String title;

    @Schema(description = "book author")
    private String author;

    @Schema(description = "current price")
    private BigDecimal price;

    public static SimpleBookInfoVO from(BookInfo bookInfo) {
        SimpleBookInfoVO entity = new SimpleBookInfoVO();
        entity.setId(bookInfo.getId());
        entity.setTitle(bookInfo.getTitle());
        entity.setAuthor(bookInfo.getAuthor());
        entity.setPrice(bookInfo.getPrice());
        return entity;
    }

}
