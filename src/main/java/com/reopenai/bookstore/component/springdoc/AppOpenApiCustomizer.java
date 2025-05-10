package com.reopenai.bookstore.component.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Optional;

/**
 * Created by Allen Huang
 */
@Configuration
public class AppOpenApiCustomizer implements OpenApiCustomizer, Ordered {

    @Override
    public void customise(OpenAPI openApi) {
        customizeInfo(openApi);
    }

    private void customizeInfo(OpenAPI openApi) {
        Info info = Optional.ofNullable(openApi.getInfo())
                .orElseGet(Info::new);
        info.setTitle("BookStore APIs");
        info.setVersion("0.1.0");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
