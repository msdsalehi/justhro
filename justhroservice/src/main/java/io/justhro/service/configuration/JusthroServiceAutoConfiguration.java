package io.justhro.service.configuration;

import io.justhro.service.advice.JustAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class JusthroServiceAutoConfiguration {

    @Bean
    @Lazy
    public ReloadableResourceBundleMessageSource errorMessages() {
        return loadMessageSource("classpath:i18n/errors");
    }

    private ReloadableResourceBundleMessageSource loadMessageSource(String bundleName) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(bundleName);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public JustAdvice justAdvice() {
        return new JustAdvice();
    }
}
