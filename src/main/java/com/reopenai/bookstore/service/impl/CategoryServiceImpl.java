package com.reopenai.bookstore.service.impl;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.entity.BookCategory;
import com.reopenai.bookstore.bean.request.AddBookCategoryRequest;
import com.reopenai.bookstore.bean.request.QueryBookCategoryRequest;
import com.reopenai.bookstore.bean.request.UpdateBookCategoryRequest;
import com.reopenai.bookstore.bean.vo.BookCategoryVO;
import com.reopenai.bookstore.component.database.condition.LambdaEaseQuery;
import com.reopenai.bookstore.component.exception.BusinessException;
import com.reopenai.bookstore.repository.BookCategoryRepository;
import com.reopenai.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Allen Huang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final R2dbcEntityTemplate template;

    private final BookCategoryRepository bookCategoryRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<BookCategoryVO> create(AddBookCategoryRequest request) {
        String name = request.getName();
        return bookCategoryRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        log.info("[BookCategory]Add failed, {} already exists", name);
                        return Mono.error(new BusinessException(ErrorCode.BOOK_CATEGORY_EXISTS, name));
                    }
                    BookCategory entity = new BookCategory();
                    entity.setName(name);
                    entity.setCreatedBy(-1L);
                    entity.setUpdatedBy(-1L);
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setUpdatedTime(entity.getCreatedTime());
                    return bookCategoryRepository.save(entity);
                })
                .map(BookCategoryVO::from);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<BookCategoryVO> update(UpdateBookCategoryRequest request) {
        return bookCategoryRepository.findById(request.getId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "id=" + request.getId()))))
                .flatMap(entity -> {
                    if (entity.getName().equals(request.getName())) {
                        return Mono.just(entity);
                    }
                    String name = request.getName();
                    return bookCategoryRepository.existsByName(name)
                            .flatMap(exists -> {
                                if (exists) {
                                    log.info("[BookCategory]Update failed, {} already exists", name);
                                    return Mono.error(new BusinessException(ErrorCode.BOOK_CATEGORY_EXISTS, name));
                                }
                                entity.setName(name);
                                entity.setUpdatedTime(LocalDateTime.now());
                                return bookCategoryRepository.save(entity);
                            });
                })
                .map(BookCategoryVO::from);
    }


    public Mono<List<BookCategoryVO>> query(QueryBookCategoryRequest request) {
        Query query = new LambdaEaseQuery<BookCategory>()
                .eq(request.getId() != null, BookCategory::getId, request.getId())
                .lt(request.getCursor() != null, BookCategory::getId, request.getCursor())
                .orderByDesc(BookCategory::getId)
                .asQuery().limit(request.getLimit());
        return template.select(query, BookCategory.class)
                .map(BookCategoryVO::from)
                .collectList();
    }

}
