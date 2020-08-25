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

public class JustResponseStatusAPIException extends JustAPIException {

    private int status;

    public JustResponseStatusAPIException(int status) {
    }

    public JustResponseStatusAPIException(String message, JustAPIExceptionDetailsProvider rootCause, int status) {
        super(message, rootCause);
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return INTERNAL_CODE_PREFIX + status;
    }
}
