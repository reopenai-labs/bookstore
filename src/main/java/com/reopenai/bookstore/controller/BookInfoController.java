package com.reopenai.bookstore.controller;

import com.reopenai.bookstore.bean.ApiResponse;
import com.reopenai.bookstore.bean.request.AddBookRequest;
import com.reopenai.bookstore.bean.request.QueryBookRequest;
import com.reopenai.bookstore.bean.request.UpdateBookRequest;
import com.reopenai.bookstore.bean.vo.BookDetailVO;
import com.reopenai.bookstore.service.BookInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "APIs for Books")
public class BookInfoController {

    private final BookInfoService bookInfoService;

    @PostMapping("/v1/admin/books")
    @Operation(summary = "Add a new book")
    public Mono<ApiResponse<BookDetailVO>> addBook(@RequestBody @Valid AddBookRequest requestBody) {
        return bookInfoService.addBook(requestBody)
                .map(ApiResponse::success);
    }

    @PutMapping("/v1/admin/books")
    @Operation(summary = "Update book information")
    public Mono<ApiResponse<BookDetailVO>> updateBook(@RequestBody @Valid UpdateBookRequest requestBody) {
        return bookInfoService.updateBook(requestBody)
                .map(ApiResponse::success);
    }

    @GetMapping("/v1/books")
    @Operation(summary = "Query book list")
    public Mono<ApiResponse<List<BookDetailVO>>> queryBooks(@ParameterObject @Valid QueryBookRequest request) {
        return bookInfoService.queryBooks(request)
                .map(ApiResponse::success);
    }

}
