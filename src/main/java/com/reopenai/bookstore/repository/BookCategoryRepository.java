package com.reopenai.bookstore.repository;

import com.reopenai.bookstore.bean.entity.BookCategory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Created by Allen Huang
 */
@Repository
public interface BookCategoryRepository extends R2dbcRepository<BookCategory, Long> {

    /**
     * Whether there is a category with the specified name
     *
     * @param name The name of the category information of the specified name
     * @return Returns true if exists, otherwise returns false
     */
    Mono<Boolean> existsByName(String name);

}
