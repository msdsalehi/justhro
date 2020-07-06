package io.justhro.client.configuration;

import feign.RequestInterceptor;
import io.justhro.client.decoder.JustErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class JusthroClientAutoConfiguration {

    @Bean
    public JustErrorDecoder clientErrorDecoder() {
        return new JustErrorDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate.header("Accept-Language", LocaleContextHolder.getLocale().getLanguage());
    }
}
