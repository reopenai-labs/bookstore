package com.reopenai.bookstore.repository;

import com.reopenai.bookstore.bean.entity.ShoppingCart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Allen Huang
 */
@Repository
public interface ShoppingCartRepository extends R2dbcRepository<ShoppingCart, Long> {

    /**
     * Remove a record based on userId and bookId
     *
     * @param userId userId
     * @param bookId bookId
     * @return The number of entries to remove
     */
    Mono<Integer> removeByUserIdAndBookId(Long userId, Long bookId);

    /**
     * Get a list of all items in the shopping cart of a specified user
     *
     * @param userId userId
     * @return List of items in the shopping cart
     */
    Flux<ShoppingCart> findAllByUserId(Long userId);

    /**
     * Get the details of a product in the shopping cart of a specified user
     *
     * @param userId userId
     * @param bookId bookId
     * @return Details of an item in the shopping cart
     */
    Mono<ShoppingCart> findByUserIdAndBookId(Long userId, Long bookId);

}
