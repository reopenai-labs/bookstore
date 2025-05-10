package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.request.AddBookRequest;
import com.reopenai.bookstore.bean.request.UpdateBookRequest;
import com.reopenai.bookstore.bean.vo.BookDetailVO;
import com.reopenai.bookstore.component.i18n.I18nConfig;
import com.reopenai.bookstore.controller.BookInfoController;
import com.reopenai.bookstore.service.BookInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Allen Huang
 */
@Import(I18nConfig.class)
@WebFluxTest(controllers = BookInfoController.class)
public class BookInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookInfoService bookInfoService;

    @BeforeEach
    public void setup() {
        when(bookInfoService.addBook(any()))
                .thenAnswer(i -> Mono.just(new BookDetailVO()));
        when(bookInfoService.updateBook(any()))
                .thenAnswer(i -> Mono.just(new BookDetailVO()));
        when(bookInfoService.queryBooks(any()))
                .thenAnswer(i -> Mono.just(Collections.emptyList()));
    }

    @ParameterizedTest
    @MethodSource("positiveAddRequestsProvider")
    public void addBooksPositiveTest(AddBookRequest request) {
        webTestClient.post()
                .uri("/v1/admin/books")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveAddRequestsProvider() {
        return Stream.of(
                Arguments.of(new AddBookRequest(1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, randomString(255), "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, "title", randomString(255), BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, "title", "author", BigDecimal.ZERO)),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("1.1234"))),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("123456789098765.123"))),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("123456789098765.1234")))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidAddRequestsProvider")
    public void addBooksNegativeTest(AddBookRequest request) {
        webTestClient.post()
                .uri("/v1/admin/books")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidAddRequestsProvider() {
        return Stream.of(
                Arguments.of(new AddBookRequest()),
                Arguments.of(new AddBookRequest(null, randomString(255), "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, null, "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, "title", null, BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, "title", "author", null)),
                Arguments.of(new AddBookRequest(0L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(-1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new AddBookRequest(1L, "", "author", BigDecimal.ZERO)),
                Arguments.of(new AddBookRequest(1L, randomString(256), "author", BigDecimal.ZERO)),
                Arguments.of(new AddBookRequest(1L, "title", "", BigDecimal.ZERO)),
                Arguments.of(new AddBookRequest(1L, "title", randomString(256), BigDecimal.ZERO)),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("-1"))),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("1234567890987651"))),
                Arguments.of(new AddBookRequest(1L, "title", "author", new BigDecimal("12345678909876.12345")))
        );
    }

    @ParameterizedTest
    @MethodSource("positiveUpdateRequestsProvider")
    public void updateBooksPositiveTest(UpdateBookRequest request) {
        webTestClient.put()
                .uri("/v1/admin/books")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveUpdateRequestsProvider() {
        return Stream.of(
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, randomString(255), "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", randomString(255), BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", BigDecimal.ZERO)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("1.1234"))),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("123456789098765.123"))),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("123456789098765.1234")))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateRequestsProvider")
    public void updateBooksNegativeTest(UpdateBookRequest request) {
        webTestClient.put()
                .uri("/v1/admin/books")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidUpdateRequestsProvider() {
        return Stream.of(
                Arguments.of(new UpdateBookRequest()),
                Arguments.of(new UpdateBookRequest(null, 1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(0L, 1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(-1L, 1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, null, randomString(255), "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, null, "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", null, BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", null)),
                Arguments.of(new UpdateBookRequest(1L, 0L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, -1L, "title", "author", BigDecimal.ONE)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "", "author", BigDecimal.ZERO)),
                Arguments.of(new UpdateBookRequest(1L, 1L, randomString(256), "author", BigDecimal.ZERO)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "", BigDecimal.ZERO)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", randomString(256), BigDecimal.ZERO)),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("-1"))),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("1234567890987651"))),
                Arguments.of(new UpdateBookRequest(1L, 1L, "title", "author", new BigDecimal("12345678909876.12345")))
        );
    }

    @ParameterizedTest
    @MethodSource("positiveQueryRequestsProvider")
    public void queryPositiveTest(Integer limit, Long cursor, Long categoryId, Long id, String title, String author) {
        webTestClient.get()
                .uri(builder -> {
                    builder
                            .path("/v1/books")
                            .queryParam("cursor", cursor)
                            .queryParam("id", id)
                            .queryParam("categoryId", categoryId)
                            .queryParam("title", title)
                            .queryParam("author", author);
                    if (limit != null) {
                        builder.queryParam("limit", limit);
                    }
                    return builder.build();
                })
                .exchange()
                .expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(null, null, null, null, null, null),
                Arguments.of(1, null, null, null, null, null),
                Arguments.of(256, null, null, null, null, null),
                Arguments.of(null, 1L, null, null, null, null),
                Arguments.of(null, null, 1L, null, null, null),
                Arguments.of(null, null, null, 1L, null, null),
                Arguments.of(null, null, null, null, randomString(255), null),
                Arguments.of(null, null, null, null, null, randomString(255))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidQueryRequestsProvider")
    public void queryNegativeTest(Integer limit, Long cursor, Long categoryId, Long id, String title, String author) {
        webTestClient.get()
                .uri(builder -> builder
                        .path("/v1/books")
                        .queryParam("limit", limit)
                        .queryParam("cursor", cursor)
                        .queryParam("id", id)
                        .queryParam("categoryId", categoryId)
                        .queryParam("title", title)
                        .queryParam("author", author)
                        .build())
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(-1, null, null, null, null, null),
                Arguments.of(0, null, null, null, null, null),
                Arguments.of(257, null, null, null, null, null),
                Arguments.of(null, -1L, null, null, null, null),
                Arguments.of(null, 0L, null, null, null, null),
                Arguments.of(null, null, -1L, null, null, null),
                Arguments.of(null, null, 0L, null, null, null),
                Arguments.of(null, null, null, -1L, null, null),
                Arguments.of(null, null, null, 0L, null, null),
                Arguments.of(null, null, null, null, randomString(256), null),
                Arguments.of(null, null, null, null, null, randomString(256))
        );
    }

    private static String randomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
