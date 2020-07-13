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

public class JustUnknownAPIException extends JustAPIException {
    public JustUnknownAPIException(String message, Throwable cause, JustAPIException rootCause) {
        super(message, cause, rootCause);
    }

    public JustUnknownAPIException(Throwable cause, JustAPIException rootCause) {
        super(cause, rootCause);
    }

    public JustUnknownAPIException(String message, JustAPIException rootCause) {
        super(message, rootCause);
    }

    public JustUnknownAPIException(JustAPIException rootCause) {
        super(rootCause);
    }

    public JustUnknownAPIException() {
    }

    @Override
    public int getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
