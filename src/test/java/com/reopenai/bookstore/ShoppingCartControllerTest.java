package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.request.AddCartItemRequest;
import com.reopenai.bookstore.bean.request.ReduceCartItemRequest;
import com.reopenai.bookstore.bean.vo.ShoppingCartVO;
import com.reopenai.bookstore.controller.ShoppingCartController;
import com.reopenai.bookstore.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Allen Huang
 */
@WebFluxTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        when(shoppingCartService.addItem(any()))
                .thenAnswer(i -> Mono.just(new ShoppingCartVO()));
        when(shoppingCartService.reduceItemQuantity(any()))
                .thenAnswer(i -> Mono.just(new ShoppingCartVO()));
        when(shoppingCartService.queryItems(any()))
                .thenAnswer(i -> Mono.just(Collections.emptyList()));
    }

    @ParameterizedTest
    @MethodSource("addRequestsProvider")
    public void addItemTest(AddCartItemRequest request, HttpStatus status) {
        webTestClient.post()
                .uri("/v1/shopping-cart/items")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(status.value());
    }

    private static Stream<Arguments> addRequestsProvider() {
        return Stream.of(
                Arguments.of(new AddCartItemRequest(1, 1L, null), HttpStatus.OK),
                Arguments.of(new AddCartItemRequest(null, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new AddCartItemRequest(-1, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new AddCartItemRequest(0, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new AddCartItemRequest(1, null, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new AddCartItemRequest(1, -1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new AddCartItemRequest(1, -0L, null), HttpStatus.BAD_REQUEST)
        );
    }

    @ParameterizedTest
    @MethodSource("reduceQuantityProvider")
    public void reduceQuantityTest(ReduceCartItemRequest request, HttpStatus status) {
        webTestClient.patch()
                .uri("/v1/shopping-cart/items")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(status.value());
    }

    private static Stream<Arguments> reduceQuantityProvider() {
        return Stream.of(
                Arguments.of(new ReduceCartItemRequest(1, 1L, null), HttpStatus.OK),
                Arguments.of(new ReduceCartItemRequest(null, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new ReduceCartItemRequest(-1, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new ReduceCartItemRequest(0, 1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new ReduceCartItemRequest(1, null, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new ReduceCartItemRequest(1, -1L, null), HttpStatus.BAD_REQUEST),
                Arguments.of(new ReduceCartItemRequest(1, -0L, null), HttpStatus.BAD_REQUEST)
        );
    }


    @ParameterizedTest
    @MethodSource("positiveQueryRequestsProvider")
    public void queryItemsPositiveTest(Integer limit, Long cursor) {
        webTestClient.get()
                .uri(builder -> {
                    builder.path("/v1/shopping-cart");
                    if (limit != null) {
                        builder.queryParam("limit", limit);
                    }
                    if (cursor != null) {
                        builder.queryParam("cursor", cursor);
                    }
                    return builder.build();
                })
                .exchange()
                .expectStatus()
                .isOk();
    }


    private static Stream<Arguments> positiveQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(1, null),
                Arguments.of(100, null),
                Arguments.of(null, 1L)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidQueryRequestsProvider")
    public void queryItemsNegativeTest(Integer limit, Long cursor) {
        webTestClient.get()
                .uri(builder -> {
                    builder.path("/v1/shopping-cart");
                    if (limit != null) {
                        builder.queryParam("limit", limit);
                    }
                    if (cursor != null) {
                        builder.queryParam("cursor", cursor);
                    }
                    return builder.build();
                })
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private static Stream<Arguments> invalidQueryRequestsProvider() {
        return Stream.of(
                Arguments.of(-1, null),
                Arguments.of(0, null),
                Arguments.of(null, 0L),
                Arguments.of(null, -1L)
        );
    }
}
