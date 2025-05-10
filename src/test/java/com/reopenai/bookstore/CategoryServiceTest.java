package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.request.AddBookCategoryRequest;
import com.reopenai.bookstore.bean.request.QueryBookCategoryRequest;
import com.reopenai.bookstore.bean.request.UpdateBookCategoryRequest;
import com.reopenai.bookstore.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by Allen Huang
 */
public class CategoryServiceTest extends BaseServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void createDuplicateEntityTest() {
        AddBookCategoryRequest request = new AddBookCategoryRequest();
        request.setName("test0");
        StepVerifier.create(categoryService.create(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.BOOK_CATEGORY_EXISTS))
                .verify();
    }

    @Test
    public void createEntityTest() {
        String name = "test case";
        AddBookCategoryRequest request = new AddBookCategoryRequest();
        request.setName(name);
        StepVerifier.create(categoryService.create(request))
                .assertNext(entity -> {
                    assertThat(entity.getName()).isEqualTo(name);
                    assertThat(entity.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    public void updateNonExistentEntityTest() {
        UpdateBookCategoryRequest request = new UpdateBookCategoryRequest();
        request.setId(-1L);
        request.setName("test");
        StepVerifier.create(categoryService.update(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.DATA_NOT_FOUND))
                .verify();
    }

    @Test
    public void updateEntityTest() {
        UpdateBookCategoryRequest request = new UpdateBookCategoryRequest();
        request.setId(1L);
        request.setName("test0");
        StepVerifier.create(categoryService.update(request))
                .assertNext(entity -> assertThat(entity.getName()).isEqualTo("test0"))
                .verifyComplete();

        request.setName("test case update entity");
        StepVerifier.create(categoryService.update(request))
                .assertNext(entity -> assertThat(entity.getName()).isEqualTo("test case update entity"))
                .verifyComplete();
    }

    @Test
    public void queryNotExistsTest() {
        QueryBookCategoryRequest request = new QueryBookCategoryRequest();
        request.setId(0L);
        StepVerifier.create(categoryService.query(request))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @Test
    public void queryByIdTest() {
        QueryBookCategoryRequest request = new QueryBookCategoryRequest();
        request.setId(1L);
        StepVerifier.create(categoryService.query(request))
                .assertNext(categories -> {
                    assertThat(categories.size()).isEqualTo(1);
                    assertThat(categories.get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    public void queryByCursorTest() {
        QueryBookCategoryRequest request = new QueryBookCategoryRequest();
        request.setId(null);
        request.setLimit(3);
        request.setCursor(8L);
        StepVerifier.create(categoryService.query(request))
                .assertNext(categories -> {
                    assertThat(categories.size()).isEqualTo(3);
                    assertThat(categories.get(0).getId()).isEqualTo(7L);
                })
                .verifyComplete();

    }

}
