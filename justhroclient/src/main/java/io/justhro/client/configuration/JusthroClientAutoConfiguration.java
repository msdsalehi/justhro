/**
 * Copyright (C) 2020  Masoud Salehi Alamdari
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
