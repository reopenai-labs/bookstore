package com.reopenai.bookstore.component.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reopenai.bookstore.component.jackson.deserializer.LocalDateTimeDeserializer;
import com.reopenai.bookstore.component.jackson.serializer.BigDecimalSerializer;
import com.reopenai.bookstore.component.jackson.serializer.LocalDateTimeSerializer;
import com.reopenai.bookstore.component.jackson.serializer.NumberSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Jackson config
 *
 * @author Allen Huang
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(Number.class, new NumberSerializer())
                .serializerByType(BigDecimal.class, new BigDecimalSerializer())
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
    }

}
