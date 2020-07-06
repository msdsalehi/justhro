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
package io.justhro.core.exception;

import org.springframework.http.HttpStatus;

public class JustBadRequestAPIException extends JustAPIException {

    public JustBadRequestAPIException() {
    }

    public JustBadRequestAPIException(String message) {
        super(message);
    }

    public JustBadRequestAPIException(String message, String localizedMessage) {
        super(message, localizedMessage);
    }

    public JustBadRequestAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustBadRequestAPIException(Throwable cause) {
        super(cause);
    }

    public JustBadRequestAPIException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.BAD_REQUEST.value();
    }
}
