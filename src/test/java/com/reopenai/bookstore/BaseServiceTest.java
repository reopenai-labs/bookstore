package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.component.exception.BusinessException;
import org.springframework.boot.test.context.SpringBootTest;

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

    protected void matchBizError(Throwable ex, ErrorCode errorCode) {
        assertThat(ex).isInstanceOf(BusinessException.class);
        assertThat(((BusinessException) ex).getErrorCode()).isEqualTo(errorCode);
    }

}
