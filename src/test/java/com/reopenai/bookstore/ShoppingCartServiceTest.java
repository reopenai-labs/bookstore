package com.reopenai.bookstore;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.entity.ShoppingCart;
import com.reopenai.bookstore.bean.request.AddCartItemRequest;
import com.reopenai.bookstore.bean.request.QueryCartItemsRequest;
import com.reopenai.bookstore.bean.request.ReduceCartItemRequest;
import com.reopenai.bookstore.bean.vo.ShoppingCartVO;
import com.reopenai.bookstore.bean.vo.SimpleCartItemVO;
import com.reopenai.bookstore.service.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Allen Huang
 */
public class ShoppingCartServiceTest extends BaseServiceTest {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private R2dbcEntityTemplate template;

    @Test
    public void addItemTest() {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setQuantity(1);
        request.setBookId(-1L);
        request.setUserId(1L);
        StepVerifier.create(shoppingCartService.addItem(request))
                .expectErrorSatisfies(ex -> matchBizError(ex, ErrorCode.DATA_NOT_FOUND))
                .verify();

        request.setBookId(1L);
        StepVerifier.create(shoppingCartService.addItem(request))
                .assertNext(item -> {
                    assertThat(item.getCreatedTime()).isEqualTo(item.getUpdatedTime());
                    assertThat(item.getQuantity()).isEqualTo(1);
                    assertThat(item.getBookInfo()).isNotNull();
                })
                .verifyComplete();

        StepVerifier.create(shoppingCartService.addItem(request))
                .assertNext(item -> {
                    assertThat(item.getCreatedTime()).isBefore(item.getUpdatedTime());
                    assertThat(item.getQuantity()).isEqualTo(2);
                    assertThat(item.getBookInfo()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    public void queryItemTest() {
        for (long i = 2; i < 10; i++) {
            AddCartItemRequest request = new AddCartItemRequest();
            request.setQuantity(1);
            request.setUserId(1L);
            request.setBookId(i);
            shoppingCartService.addItem(request).block();
        }

        QueryCartItemsRequest queryRequest = new QueryCartItemsRequest();
        queryRequest.setUserId(1L);
        StepVerifier.create(shoppingCartService.queryItems(queryRequest))
                .assertNext(entities -> {
                    assertThat(entities).isNotEmpty();
                    for (ShoppingCartVO entity : entities) {
                        assertThat(entity.getBookInfo()).isNotNull();
                    }
                })
                .verifyComplete();

        queryRequest.setLimit(2);
        StepVerifier.create(shoppingCartService.queryItems(queryRequest))
                .assertNext(entities -> {
                    assertThat(entities.size()).isEqualTo(2);
                    for (ShoppingCartVO entity : entities) {
                        assertThat(entity.getBookInfo()).isNotNull();
                    }
                })
                .verifyComplete();

        queryRequest.setCursor(3L);
        StepVerifier.create(shoppingCartService.queryItems(queryRequest))
                .assertNext(entities -> {
                    assertThat(entities.size()).isEqualTo(2);
                    assertThat(entities.get(0).getId()).isEqualTo(2L);
                    for (ShoppingCartVO entity : entities) {
                        assertThat(entity.getBookInfo()).isNotNull();
                    }
                })
                .verifyComplete();
    }

    @Test
    public void removeItemTest() {
        template.delete(Query.empty(), ShoppingCart.class).block();
        StepVerifier.create(shoppingCartService.removeItem(1L, 1L))
                .assertNext(result -> assertThat(result).isFalse())
                .verifyComplete();

        AddCartItemRequest request = new AddCartItemRequest();
        request.setQuantity(1);
        request.setUserId(1L);
        request.setBookId(1L);
        shoppingCartService.addItem(request).block();

        StepVerifier.create(shoppingCartService.removeItem(1L, 1L))
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();

        StepVerifier.create(shoppingCartService.removeItem(1L, 1L))
                .assertNext(result -> assertThat(result).isFalse())
                .verifyComplete();
    }

    @Test
    public void reduceItemQuantityTest() {
        template.delete(Query.empty(), ShoppingCart.class).block();

        ReduceCartItemRequest request = new ReduceCartItemRequest();
        request.setQuantity(1);
        request.setUserId(1L);
        request.setBookId(1L);

        StepVerifier.create(shoppingCartService.reduceItemQuantity(request))
                .assertNext(entity -> {
                    assertThat(entity.getCreatedTime()).isEqualTo(entity.getUpdatedTime());
                    assertThat(entity.getQuantity()).isEqualTo(0);
                    assertThat(entity.getBookInfo()).isNotNull();
                })
                .verifyComplete();

        AddCartItemRequest addRequest = new AddCartItemRequest();
        addRequest.setQuantity(2);
        addRequest.setUserId(1L);
        addRequest.setBookId(1L);
        shoppingCartService.addItem(addRequest).block();

        StepVerifier.create(shoppingCartService.reduceItemQuantity(request))
                .assertNext(entity -> {
                    assertThat(entity.getCreatedTime()).isBefore(entity.getUpdatedTime());
                    assertThat(entity.getQuantity()).isEqualTo(1);
                    assertThat(entity.getBookInfo()).isNotNull();
                })
                .verifyComplete();

        StepVerifier.create(shoppingCartService.reduceItemQuantity(request))
                .assertNext(entity -> {
                    assertThat(entity.getCreatedTime()).isBefore(entity.getUpdatedTime());
                    assertThat(entity.getQuantity()).isEqualTo(0);
                    assertThat(entity.getBookInfo()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    public void checkoutTest() {
        template.delete(Query.empty(), ShoppingCart.class).block();
        StepVerifier.create(shoppingCartService.checkout(1L))
                .assertNext(entity -> {
                    assertThat(entity.getItems()).isEmpty();
                    assertThat(entity.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
                })
                .verifyComplete();

        for (long i = 1; i < 5; i++) {
            AddCartItemRequest request = new AddCartItemRequest();
            request.setQuantity(1);
            request.setUserId(1L);
            request.setBookId(i);
            shoppingCartService.addItem(request).block();
        }

        StepVerifier.create(shoppingCartService.checkout(1L))
                .assertNext(entity -> {
                    assertThat(entity.getItems()).isNotEmpty();
                    BigDecimal amount = BigDecimal.ZERO;
                    for (SimpleCartItemVO item : entity.getItems()) {
                        assertThat(item.getTotalPrice()).isEqualTo(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                        amount = amount.add(item.getTotalPrice());
                    }
                    assertThat(entity.getTotalPrice()).isEqualTo(amount);
                })
                .verifyComplete();
    }

}
