package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.request.AddBookRequest;
import com.reopenai.bookstore.bean.request.QueryBookRequest;
import com.reopenai.bookstore.bean.request.UpdateBookRequest;
import com.reopenai.bookstore.bean.vo.BookDetailVO;
import com.reopenai.bookstore.service.BookInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by Allen Huang
 */
public class BookInfoServiceTest extends BaseServiceTest {

    @Autowired
    private BookInfoService bookInfoService;

    @Test
    public void addBookTest() {
        AddBookRequest request = new AddBookRequest();
        request.setTitle("add book test");
        request.setAuthor("add book test");
        request.setCategoryId(1L);
        request.setPrice(BigDecimal.TEN);
        StepVerifier.create(bookInfoService.addBook(request))
                .assertNext(bookInfo -> {
                    assertThat(bookInfo.getId()).isNotNull();
                    assertThat(bookInfo.getCategoryId()).isEqualTo(1L);
                    assertThat(bookInfo.getCategoryName()).isNotBlank();
                })
                .verifyComplete();

        // category not exists case
        request.setCategoryId(-1L);
        StepVerifier.create(bookInfoService.addBook(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.DATA_NOT_FOUND))
                .verify();
    }

    @Test
    public void updateBookTest() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setId(-1L);
        StepVerifier.create(bookInfoService.updateBook(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.DATA_NOT_FOUND))
                .verify();

        request.setId(1L);
        request.setCategoryId(2L);
        request.setTitle("update book test");
        request.setAuthor("update book test");
        request.setPrice(BigDecimal.TEN);

        StepVerifier.create(bookInfoService.updateBook(request))
                .assertNext(bookInfo -> {
                    assertThat(bookInfo.getId()).isEqualTo(1L);
                    assertThat(bookInfo.getCategoryId()).isEqualTo(1L);
                    assertThat(bookInfo.getCategoryName()).isNotBlank();
                })
                .verifyComplete();

        // category not exists case
        request.setId(1L);
        request.setCategoryId(-1L);
        StepVerifier.create(bookInfoService.updateBook(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.DATA_NOT_FOUND))
                .verify();
    }

    @Test
    public void queryByAuthorTest() {
        QueryBookRequest request = new QueryBookRequest();
        request.setAuthor("A");
        StepVerifier.create(bookInfoService.queryBooks(request))
                .assertNext(entities -> {
                    assertThat(entities.size()).isGreaterThan(0);
                    for (BookDetailVO book : entities) {
                        assertThat(book.getAuthor()).contains("A");
                    }
                })
                .verifyComplete();
    }


    @Test
    public void queryByTitleTest() {
        QueryBookRequest request = new QueryBookRequest();
        request.setTitle("T");
        StepVerifier.create(bookInfoService.queryBooks(request))
                .assertNext(entities -> {
                    assertThat(entities.size()).isGreaterThan(0);
                    for (BookDetailVO book : entities) {
                        assertThat(book.getTitle()).contains("T");
                    }
                })
                .verifyComplete();
    }

    @Test
    public void queryByCategoryTest() {
        QueryBookRequest request = new QueryBookRequest();
        request.setCategoryId(-1L);
        StepVerifier.create(bookInfoService.queryBooks(request))
                .expectNext(Collections.emptyList())
                .verifyComplete();

        request.setCategoryId(1L);
        StepVerifier.create(bookInfoService.queryBooks(request))
                .assertNext(entities -> {
                    assertThat(entities.size()).isGreaterThan(0);
                    for (BookDetailVO book : entities) {
                        assertThat(book.getCategoryId()).isEqualTo(1L);
                        assertThat(book.getCategoryName()).isNotBlank();
                    }
                })
                .verifyComplete();
    }

    @Test
    public void queryByBookIdTest() {
        QueryBookRequest request = new QueryBookRequest();
        request.setId(-1L);
        StepVerifier.create(bookInfoService.queryBooks(request))
                .expectNext(Collections.emptyList())
                .verifyComplete();

        request.setId(1L);
        StepVerifier.create(bookInfoService.queryBooks(request))
                .assertNext(books -> {
                    assertThat(books.size()).isEqualTo(1);
                    BookDetailVO book = books.get(0);
                    assertThat(book.getId()).isEqualTo(1L);
                    assertThat(book.getCategoryId()).isNotNull();
                    assertThat(book.getCategoryName()).isNotBlank();
                })
                .verifyComplete();
    }

    @Test
    public void queryByCursorTest() {
        QueryBookRequest request = new QueryBookRequest();
        request.setLimit(5);
        request.setCursor(8L);
        StepVerifier.create(bookInfoService.queryBooks(request))
                .assertNext(books -> {
                    assertThat(books.size()).isEqualTo(5);
                    BookDetailVO book = books.get(0);
                    assertThat(book.getId()).isEqualTo(7L);
                    assertThat(book.getCategoryId()).isNotNull();
                    assertThat(book.getCategoryName()).isNotBlank();
                })
                .verifyComplete();
    }


}
