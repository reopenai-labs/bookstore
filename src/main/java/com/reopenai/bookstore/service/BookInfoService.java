package com.reopenai.bookstore.service;

import com.reopenai.bookstore.bean.request.AddBookRequest;
import com.reopenai.bookstore.bean.request.QueryBookRequest;
import com.reopenai.bookstore.bean.request.UpdateBookRequest;
import com.reopenai.bookstore.bean.vo.BookDetailVO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
public interface BookInfoService {

    /**
     * Add a book information
     *
     * @param request request parameter
     * @return added book details
     */
    Mono<BookDetailVO> addBook(AddBookRequest request);

    /**
     * Update a book information
     *
     * @param request request parameter
     * @return updated book information
     */
    Mono<BookDetailVO> updateBook(UpdateBookRequest request);

    /**
     * Query the book information list
     *
     * @param request query request parameters
     * @return book list
     */
    Mono<List<BookDetailVO>> queryBooks(QueryBookRequest request);

}
