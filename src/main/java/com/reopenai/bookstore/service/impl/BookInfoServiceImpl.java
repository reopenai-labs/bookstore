package com.reopenai.bookstore.service.impl;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.entity.BookCategory;
import com.reopenai.bookstore.bean.entity.BookInfo;
import com.reopenai.bookstore.bean.request.AddBookRequest;
import com.reopenai.bookstore.bean.request.QueryBookRequest;
import com.reopenai.bookstore.bean.request.UpdateBookRequest;
import com.reopenai.bookstore.bean.vo.BookDetailVO;
import com.reopenai.bookstore.component.database.condition.LambdaEaseQuery;
import com.reopenai.bookstore.component.exception.BusinessException;
import com.reopenai.bookstore.repository.BookCategoryRepository;
import com.reopenai.bookstore.repository.BookInfoRepository;
import com.reopenai.bookstore.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Allen Huang
 */
@Service
@RequiredArgsConstructor
public class BookInfoServiceImpl implements BookInfoService {

    private final BookCategoryRepository bookCategoryRepository;

    private final BookInfoRepository bookInfoRepository;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Transactional(rollbackFor = Exception.class)
    public Mono<BookDetailVO> addBook(AddBookRequest request) {
        Long categoryId = request.getCategoryId();
        return bookCategoryRepository.existsById(categoryId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "categoryId=" + categoryId));
                    }
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setCategoryId(categoryId);
                    bookInfo.setTitle(request.getTitle());
                    bookInfo.setPrice(request.getPrice());
                    bookInfo.setAuthor(request.getAuthor());
                    return bookInfoRepository.save(bookInfo)
                            .flatMap(this::converterBookDetailVO);
                });
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<BookDetailVO> updateBook(UpdateBookRequest request) {
        return bookInfoRepository.findById(request.getId())
                .flatMap(entity -> {
                    Long categoryId = request.getCategoryId();
                    return bookCategoryRepository.existsById(categoryId)
                            .flatMap(exists -> {
                                if (!exists) {
                                    return Mono.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "categoryId=" + categoryId));
                                }
                                entity.setPrice(request.getPrice());
                                entity.setTitle(request.getTitle());
                                entity.setAuthor(request.getAuthor());
                                return bookInfoRepository.save(entity);
                            });
                })
                .flatMap(this::converterBookDetailVO)
                .switchIfEmpty(Mono.create(sink ->
                        sink.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "id=" + request.getId()))
                ));
    }

    public Mono<List<BookDetailVO>> queryBooks(QueryBookRequest request) {
        Query query = new LambdaEaseQuery<BookDetailVO>()
                .eq(request.getId() != null, BookDetailVO::getId, request.getId())
                .lt(request.getCursor() != null, BookDetailVO::getId, request.getCursor())
                .like(StringUtils.hasText(request.getTitle()), BookDetailVO::getTitle, request.getTitle())
                .like(StringUtils.hasText(request.getAuthor()), BookDetailVO::getAuthor, request.getAuthor())
                .eq(request.getCategoryId() != null, BookDetailVO::getCategoryId, request.getCategoryId())
                .orderByDesc(BookDetailVO::getId)
                .asQuery()
                .limit(request.getLimit());
        return r2dbcEntityTemplate.select(query, BookInfo.class)
                .collectList()
                .filter(entities -> !entities.isEmpty())
                .flatMap(entities -> {
                    Set<Long> categoryIds = entities.stream()
                            .map(BookInfo::getCategoryId)
                            .collect(Collectors.toSet());
                    return bookCategoryRepository.findAllById(categoryIds)
                            .collect(Collectors.toMap(BookCategory::getId, BookCategory::getName))
                            .map(categories -> {
                                List<BookDetailVO> result = new ArrayList<>(entities.size());
                                for (BookInfo entity : entities) {
                                    BookDetailVO detail = BookDetailVO.from(entity);
                                    detail.setCategoryName(categories.get(entity.getCategoryId()));
                                    result.add(detail);
                                }
                                return result;
                            });
                })
                .defaultIfEmpty(Collections.emptyList());
    }

    private Mono<BookDetailVO> converterBookDetailVO(BookInfo bookInfo) {
        return bookCategoryRepository.findById(bookInfo.getCategoryId())
                .map(BookCategory::getName)
                .defaultIfEmpty("")
                .map(categoryName -> {
                    BookDetailVO result = BookDetailVO.from(bookInfo);
                    result.setCategoryName(categoryName);
                    return result;
                });
    }

}
