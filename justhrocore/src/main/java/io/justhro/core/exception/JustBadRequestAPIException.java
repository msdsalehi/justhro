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
package io.justhro.core.exception;

import org.springframework.http.HttpStatus;

public class JustBadRequestAPIException extends JustAPIException {

    public JustBadRequestAPIException() {
    }

    public JustBadRequestAPIException(JustAPIException rootCause) {
        super(rootCause);
    }

    public JustBadRequestAPIException(String message, JustAPIException rootCause) {
        super(message, rootCause);
    }

    public JustBadRequestAPIException(String message, Throwable cause, JustAPIException rootCause) {
        super(message, cause, rootCause);
    }

    public JustBadRequestAPIException(Throwable cause, JustAPIException rootCause) {
        super(cause, rootCause);
    }

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.BAD_REQUEST.value();
    }
}
