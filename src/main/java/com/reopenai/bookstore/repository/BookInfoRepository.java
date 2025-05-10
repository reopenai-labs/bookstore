package com.reopenai.bookstore.repository;

import com.reopenai.bookstore.bean.entity.BookInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Allen Huang
 */
@Repository
public interface BookInfoRepository extends R2dbcRepository<BookInfo, Long> {
}
