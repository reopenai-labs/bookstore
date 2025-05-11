package com.reopenai.bookstore.service;

import com.reopenai.bookstore.bean.request.AddCartItemRequest;
import com.reopenai.bookstore.bean.request.QueryCartItemsRequest;
import com.reopenai.bookstore.bean.request.ReduceCartItemRequest;
import com.reopenai.bookstore.bean.vo.CartCheckoutVO;
import com.reopenai.bookstore.bean.vo.ShoppingCartVO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
public interface ShoppingCartService {

    /**
     * Add items to cart
     *
     * @param request add request
     * @return cart item
     */
    Mono<ShoppingCartVO> addItem(AddCartItemRequest request);

    /**
     * Query the list of items in the shopping cart
     *
     * @param request Query request parameters
     * @return List of items in the shopping cart
     */
    Mono<List<ShoppingCartVO>> queryItems(QueryCartItemsRequest request);

    /**
     * Remove a book from your shopping cart
     *
     * @param userId userId
     * @param bookId bookId
     * @return Returns true if there is data to be removed, otherwise returns false
     */
    Mono<Boolean> removeItem(Long userId, Long bookId);

    /**
     * Reduce the quantity of an item in the shopping cart
     *
     * @param request request
     * @return cart item
     */
    Mono<ShoppingCartVO> reduceItemQuantity(ReduceCartItemRequest request);

    /**
     * Check out the total amount of all items in the shopping cart
     *
     * @param userId userId
     * @return Settlement Details
     */
    Mono<CartCheckoutVO> checkout(Long userId);

}
