package com.reopenai.bookstore.service;

import com.reopenai.bookstore.bean.request.AddBookCategoryRequest;
import com.reopenai.bookstore.bean.request.QueryBookCategoryRequest;
import com.reopenai.bookstore.bean.request.UpdateBookCategoryRequest;
import com.reopenai.bookstore.bean.vo.BookCategoryVO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
public interface CategoryService {

    /**
     * Create a book category information
     *
     * @param request request parameter
     * @return added category information details
     */
    Mono<BookCategoryVO> create(AddBookCategoryRequest request);

    /**
     * Modify book category information
     *
     * @param request request parameter
     * @return modified category information details
     */
    Mono<BookCategoryVO> update(UpdateBookCategoryRequest request);

    /**
     * Query the book category information list
     *
     * @param request query request parameters
     * @return category information details list
     */
    Mono<List<BookCategoryVO>> query(QueryBookCategoryRequest request);

}
