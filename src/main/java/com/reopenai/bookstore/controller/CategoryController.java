package com.reopenai.bookstore.controller;

import com.reopenai.bookstore.bean.ApiResponse;
import com.reopenai.bookstore.bean.request.AddBookCategoryRequest;
import com.reopenai.bookstore.bean.request.QueryBookCategoryRequest;
import com.reopenai.bookstore.bean.request.UpdateBookCategoryRequest;
import com.reopenai.bookstore.bean.vo.BookCategoryVO;
import com.reopenai.bookstore.service.CategoryService;
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
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "APIs for Book Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @Operation(summary = "Create a book category record")
    public Mono<ApiResponse<BookCategoryVO>> create(@RequestBody @Valid AddBookCategoryRequest requestBody) {
        return categoryService.create(requestBody)
                .map(ApiResponse::success);
    }

    @PutMapping("/admin/categories")
    @Operation(summary = "Update a book category record")
    public Mono<ApiResponse<BookCategoryVO>> update(@RequestBody @Valid UpdateBookCategoryRequest requestBody) {
        return categoryService.update(requestBody)
                .map(ApiResponse::success);
    }

    @GetMapping("/categories")
    @Operation(summary = "Query Category List")
    public Mono<ApiResponse<List<BookCategoryVO>>> query(@ParameterObject @Valid QueryBookCategoryRequest request) {
        return categoryService.query(request)
                .map(ApiResponse::success);
    }

}
