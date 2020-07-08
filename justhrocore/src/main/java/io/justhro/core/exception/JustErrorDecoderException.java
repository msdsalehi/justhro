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

public class JustErrorDecoderException extends JustAPIException {

    private int httpStatus;

    public JustErrorDecoderException(String message, Throwable cause, int httpStatus) {
        super(message, cause, null);
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException(String message, int httpStatus) {
        super(message, null);
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException(int httpStatus) {
        super(null);
        this.httpStatus = httpStatus;
    }

    public JustErrorDecoderException() {
        super(null);
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + httpStatus;
    }
}
