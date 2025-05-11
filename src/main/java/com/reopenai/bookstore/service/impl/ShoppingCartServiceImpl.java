package com.reopenai.bookstore.service.impl;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.bean.entity.BookInfo;
import com.reopenai.bookstore.bean.entity.ShoppingCart;
import com.reopenai.bookstore.bean.request.AddCartItemRequest;
import com.reopenai.bookstore.bean.request.QueryCartItemsRequest;
import com.reopenai.bookstore.bean.request.ReduceCartItemRequest;
import com.reopenai.bookstore.bean.vo.CartCheckoutVO;
import com.reopenai.bookstore.bean.vo.ShoppingCartVO;
import com.reopenai.bookstore.bean.vo.SimpleBookInfoVO;
import com.reopenai.bookstore.bean.vo.SimpleCartItemVO;
import com.reopenai.bookstore.component.database.condition.LambdaEaseQuery;
import com.reopenai.bookstore.component.exception.BusinessException;
import com.reopenai.bookstore.repository.BookInfoRepository;
import com.reopenai.bookstore.repository.ShoppingCartRepository;
import com.reopenai.bookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Allen Huang
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final BookInfoRepository bookInfoRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<ShoppingCartVO> addItem(AddCartItemRequest request) {
        Long bookId = request.getBookId();
        Long userId = request.getUserId();
        return bookInfoRepository.findById(bookId)
                .switchIfEmpty(Mono.create(sink -> sink.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "bookId=" + bookId))))
                .flatMap(bookInfo ->
                        shoppingCartRepository.findByUserIdAndBookId(userId, bookId)
                                .flatMap(cart -> {
                                    Integer quantity = cart.getQuantity();
                                    cart.setQuantity(quantity + request.getQuantity());
                                    cart.setUpdatedTime(LocalDateTime.now());
                                    return shoppingCartRepository.save(cart);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    ShoppingCart shoppingCart = new ShoppingCart();
                                    shoppingCart.setUserId(userId);
                                    shoppingCart.setBookId(bookId);
                                    shoppingCart.setQuantity(request.getQuantity());
                                    shoppingCart.setCreatedTime(LocalDateTime.now());
                                    shoppingCart.setUpdatedTime(shoppingCart.getCreatedTime());
                                    return shoppingCartRepository.save(shoppingCart);
                                }))
                                .map(entity -> {
                                    ShoppingCartVO result = ShoppingCartVO.from(entity);
                                    result.setBookInfo(SimpleBookInfoVO.from(bookInfo));
                                    return result;
                                })
                );
    }

    @Override
    public Mono<List<ShoppingCartVO>> queryItems(QueryCartItemsRequest request) {
        Query query = new LambdaEaseQuery<ShoppingCart>()
                .eq(ShoppingCart::getUserId, request.getUserId())
                .lt(request.getCursor() != null, ShoppingCart::getId, request.getCursor())
                .orderByDesc(ShoppingCart::getId)
                .asQuery()
                .limit(request.getLimit());
        return r2dbcEntityTemplate.select(query, ShoppingCart.class)
                .collectList()
                .filter(entities -> !entities.isEmpty())
                .flatMap(entities -> {
                    Set<Long> bookIds = entities.stream()
                            .map(ShoppingCart::getBookId)
                            .collect(Collectors.toSet());
                    return bookInfoRepository.findAllById(bookIds)
                            .collect(Collectors.toMap(BookInfo::getId, SimpleBookInfoVO::from))
                            .map(bookMap -> {
                                List<ShoppingCartVO> elements = new ArrayList<>(entities.size());
                                for (ShoppingCart entity : entities) {
                                    ShoppingCartVO result = ShoppingCartVO.from(entity);
                                    result.setBookInfo(bookMap.get(entity.getBookId()));
                                    elements.add(result);
                                }
                                return elements;
                            });
                })
                .defaultIfEmpty(Collections.emptyList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> removeItem(Long userId, Long bookId) {
        return shoppingCartRepository.removeByUserIdAndBookId(userId, bookId)
                .map(count -> count > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<ShoppingCartVO> reduceItemQuantity(ReduceCartItemRequest request) {
        Long userId = request.getUserId();
        Long bookId = request.getBookId();
        Integer quantity = request.getQuantity();
        return bookInfoRepository.findById(bookId)
                .switchIfEmpty(Mono.create(sink -> sink.error(new BusinessException(ErrorCode.DATA_NOT_FOUND, "bookId=" + bookId))))
                .flatMap(bookInfo -> shoppingCartRepository.findByUserIdAndBookId(userId, bookId)
                        .flatMap(entity -> {
                            Integer currentQty = entity.getQuantity();
                            if (quantity.compareTo(currentQty) >= 0) {
                                return shoppingCartRepository.removeByUserIdAndBookId(userId, bookId)
                                        .map(count -> {
                                            entity.setQuantity(0);
                                            return entity;
                                        });
                            }
                            entity.setQuantity(currentQty - quantity);
                            entity.setUpdatedTime(LocalDateTime.now());
                            return shoppingCartRepository.save(entity);
                        })
                        .switchIfEmpty(Mono.create(sink -> {
                            ShoppingCart shoppingCart = new ShoppingCart();
                            shoppingCart.setUserId(userId);
                            shoppingCart.setBookId(bookId);
                            shoppingCart.setQuantity(0);
                            shoppingCart.setCreatedTime(LocalDateTime.now());
                            shoppingCart.setUpdatedTime(shoppingCart.getCreatedTime());
                            sink.success(shoppingCart);
                        }))
                        .map(entity -> {
                            ShoppingCartVO result = ShoppingCartVO.from(entity);
                            result.setBookInfo(SimpleBookInfoVO.from(bookInfo));
                            return result;
                        })
                );
    }

    @Override
    public Mono<CartCheckoutVO> checkout(Long userId) {
        return shoppingCartRepository.findAllByUserId(userId)
                .collectList()
                .filter(entities -> !entities.isEmpty())
                .flatMap(entities -> {
                    Set<Long> bookIds = entities.stream()
                            .map(ShoppingCart::getBookId)
                            .collect(Collectors.toSet());
                    return bookInfoRepository.findAllById(bookIds)
                            .collect(Collectors.toMap(BookInfo::getId, Function.identity()))
                            .map(bookMap -> {
                                BigDecimal totalPrice = BigDecimal.ZERO;
                                List<SimpleCartItemVO> elements = new ArrayList<>(entities.size());
                                for (ShoppingCart entity : entities) {
                                    BookInfo bookInfo = bookMap.get(entity.getBookId());
                                    SimpleCartItemVO item = new SimpleCartItemVO();
                                    item.setBookId(bookInfo.getId());
                                    item.setPrice(bookInfo.getPrice());
                                    item.setTitle(bookInfo.getTitle());
                                    item.setAuthor(bookInfo.getAuthor());
                                    Integer quantity = entity.getQuantity();
                                    item.setQuantity(quantity);
                                    BigDecimal totalItemPrice = bookInfo.getPrice().multiply(new BigDecimal(quantity));
                                    item.setTotalPrice(totalItemPrice);
                                    totalPrice = totalPrice.add(totalItemPrice);
                                    elements.add(item);
                                }
                                CartCheckoutVO entity = new CartCheckoutVO();
                                entity.setItems(elements);
                                entity.setTotalPrice(totalPrice);
                                return entity;
                            });
                })
                .switchIfEmpty(Mono.create(sink -> {
                    CartCheckoutVO entity = new CartCheckoutVO();
                    entity.setItems(Collections.emptyList());
                    entity.setTotalPrice(BigDecimal.ZERO);
                    sink.success(entity);
                }));
    }

}
