package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.request.AddBookCategoryRequest;
import com.reopenai.bookstore.bean.request.UpdateBookCategoryRequest;
import com.reopenai.bookstore.bean.vo.BookCategoryVO;
import com.reopenai.bookstore.controller.CategoryController;
import com.reopenai.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        when(categoryService.create(any()))
                .thenAnswer(i -> Mono.just(new BookCategoryVO()));
        when(categoryService.update(any()))
                .thenAnswer(i -> Mono.just(new BookCategoryVO()));
        when(categoryService.query(any()))
                .thenAnswer(i -> Mono.just(Collections.emptyList()));
    }

    @ParameterizedTest
    @MethodSource("positiveAddRequestsProvider")
    public void createPositiveTest(AddBookCategoryRequest request) {
        webTestClient.post()
                .uri("/v1/admin/categories")
                .bodyValue(request)
                .exchange().expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveAddRequestsProvider() {
        return Stream.of(
                Arguments.of(new AddBookCategoryRequest("test")),
                Arguments.of(new AddBookCategoryRequest(randomString(255)))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidAddRequestsProvider")
    public void createNegativeTest(AddBookCategoryRequest request) {
        webTestClient.post()
                .uri("/v1/admin/categories")
                .bodyValue(request)
                .exchange().expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidAddRequestsProvider() {
        return Stream.of(
                Arguments.of(new AddBookCategoryRequest()),
                Arguments.of(new AddBookCategoryRequest("")),
                Arguments.of(new AddBookCategoryRequest(randomString(256)))
        );
    }

    @ParameterizedTest
    @MethodSource("positiveUpdateRequestsProvider")
    public void updatePositiveTest(UpdateBookCategoryRequest request) {
        webTestClient.put()
                .uri("/v1/admin/categories")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveUpdateRequestsProvider() {
        return Stream.of(
                Arguments.of(new UpdateBookCategoryRequest(1L, "test")),
                Arguments.of(new UpdateBookCategoryRequest(1L, randomString(255)))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateRequestsProvider")
    public void updateNegativeTest(UpdateBookCategoryRequest request) {
        webTestClient.put()
                .uri("/v1/admin/categories")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidUpdateRequestsProvider() {
        return Stream.of(
                Arguments.of(new UpdateBookCategoryRequest(null, "test")),
                Arguments.of(new UpdateBookCategoryRequest(-1L, "test")),
                Arguments.of(new UpdateBookCategoryRequest(0L, "test")),
                Arguments.of(new UpdateBookCategoryRequest(1L, null)),
                Arguments.of(new UpdateBookCategoryRequest(1L, "")),
                Arguments.of(new UpdateBookCategoryRequest(1L, randomString(256)))
        );
    }

    @ParameterizedTest
    @MethodSource("positiveQueryRequestsProvider")
    public void queryPositiveTest(Integer limit, Long cursor, Long id) {
        webTestClient.get()
                .uri(builder -> builder
                        .path("/v1/categories")
                        .queryParam("limit", limit)
                        .queryParam("cursor", cursor)
                        .queryParam("id", id)
                        .build())
                .exchange()
                .expectStatus()
                .isOk();
    }

    private static Stream<Arguments> positiveQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(1, null, null),
                Arguments.of(1024, null, null),
                Arguments.of(1, 1L, null),
                Arguments.of(1, null, 1L),
                Arguments.of(1, 1L, 1L)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidQueryRequestsProvider")
    public void queryNegativeTest(Integer limit, Long cursor, Long id) {
        webTestClient.get()
                .uri(builder -> builder
                        .path("/v1/categories")
                        .queryParam("limit", limit)
                        .queryParam("cursor", cursor)
                        .queryParam("id", id)
                        .build())
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(-1, null, null),
                Arguments.of(0, null, null),
                Arguments.of(1025, null, null),
                Arguments.of(null, -1L, null),
                Arguments.of(null, 0L, null),
                Arguments.of(null, null, 0L),
                Arguments.of(null, null, -1L)
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
