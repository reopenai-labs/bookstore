package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.entity.BookCategory;
import com.reopenai.bookstore.bean.entity.BookInfo;
import com.reopenai.bookstore.component.exception.BusinessException;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Allen Huang
 */
@SpringBootTest(properties = {
        "spring.r2dbc.url=r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1",
        "spring.r2dbc.username=sa",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:ddl/v1.sql"
})
public class BaseServiceTest {

    @BeforeAll
    public static void dataInitialize(@Autowired R2dbcEntityTemplate template) {
        for (int i = 0; i < 10; i++) {
            BookCategory category = new BookCategory();
            category.setCreatedBy(1L);
            category.setUpdatedBy(1L);
            category.setCreatedTime(LocalDateTime.now());
            category.setUpdatedTime(LocalDateTime.now());
            category.setName("test" + i);
            template.insert(category).block();
        }

        for (int i = 0; i < 10; i++) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setCategoryId(i + 1L);
            bookInfo.setTitle("Test Title " + i);
            bookInfo.setAuthor("Test Author " + i);
            bookInfo.setPrice(new BigDecimal(100 + i));
            bookInfo.setCreatedBy(1L);
            bookInfo.setUpdatedBy(1L);
            bookInfo.setCreatedTime(LocalDateTime.now());
            bookInfo.setUpdatedTime(LocalDateTime.now());
            template.insert(bookInfo).block();
        }

    }

    protected void matchBizError(Throwable ex, ErrorCode errorCode) {
        assertThat(ex).isInstanceOf(BusinessException.class);
        assertThat(((BusinessException) ex).getErrorCode()).isEqualTo(errorCode);
    }

}
