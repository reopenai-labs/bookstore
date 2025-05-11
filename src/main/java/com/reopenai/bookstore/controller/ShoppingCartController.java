package com.reopenai.bookstore.controller;

import com.reopenai.bookstore.bean.ApiResponse;
import com.reopenai.bookstore.bean.request.AddCartItemRequest;
import com.reopenai.bookstore.bean.request.QueryCartItemsRequest;
import com.reopenai.bookstore.bean.request.ReduceCartItemRequest;
import com.reopenai.bookstore.bean.vo.CartCheckoutVO;
import com.reopenai.bookstore.bean.vo.ShoppingCartVO;
import com.reopenai.bookstore.component.webflux.AttrKeys;
import com.reopenai.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "APIs for Shopping cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/shopping-cart/items")
    @Operation(summary = "Add items to cart")
    public Mono<ApiResponse<ShoppingCartVO>> addItem(@RequestBody @Valid AddCartItemRequest requestBody,
                                                     @RequestAttribute(AttrKeys.CURRENT_UID) Long userId) {
        requestBody.setUserId(userId);
        return shoppingCartService.addItem(requestBody)
                .map(ApiResponse::success);
    }

    @DeleteMapping("/shopping-cart/items")
    @Operation(summary = "Remove a book from your shopping cart")
    public Mono<ApiResponse<Boolean>> removeItem(@RequestParam @Min(value = 1, message = "invalid bookId") Long bookId,
                                                 @RequestAttribute(AttrKeys.CURRENT_UID) Long userId) {
        return shoppingCartService.removeItem(userId, bookId)
                .map(ApiResponse::success);
    }

    @PatchMapping("/shopping-cart/items")
    @Operation(summary = "Reduce the quantity of an item in the shopping cart")
    public Mono<ApiResponse<ShoppingCartVO>> reduceQuantity(@RequestBody @Valid ReduceCartItemRequest requestBody,
                                                            @RequestAttribute(AttrKeys.CURRENT_UID) Long userId) {
        requestBody.setUserId(userId);
        return shoppingCartService.reduceItemQuantity(requestBody)
                .map(ApiResponse::success);
    }

    @GetMapping("/shopping-cart")
    @Operation(summary = "Get the list of items in the shopping cart")
    public Mono<ApiResponse<List<ShoppingCartVO>>> queryItems(@ParameterObject @Valid QueryCartItemsRequest request,
                                                              @RequestAttribute(AttrKeys.CURRENT_UID) Long userId) {
        request.setUserId(userId);
        return shoppingCartService.queryItems(request)
                .map(ApiResponse::success);
    }

    @PostMapping("/shopping-chart:checkout")
    @Operation(summary = "Check out all items in the shopping cart")
    public Mono<ApiResponse<CartCheckoutVO>> checkout(@RequestAttribute(AttrKeys.CURRENT_UID) Long userId) {
        return shoppingCartService.checkout(userId)
                .map(ApiResponse::success);
    }
}
