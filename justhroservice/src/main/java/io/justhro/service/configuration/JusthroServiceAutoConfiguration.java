/**
 * Copyright (C) 2020  Masoud Salehi Alamdari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
