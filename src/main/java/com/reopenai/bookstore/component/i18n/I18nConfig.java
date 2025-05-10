package com.reopenai.bookstore.component.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Allen Huang
 */
@Configuration
public class I18nConfig {

    public I18nConfig(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

}
