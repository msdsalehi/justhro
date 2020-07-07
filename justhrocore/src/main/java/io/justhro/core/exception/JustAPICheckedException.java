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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "apiMessageArgs", "localizedMessage"},
        ignoreUnknown = true)
public abstract class JustAPICheckedException extends Exception {

    static final String INTERNAL_CODE_PREFIX = "*-";
    private Object[] apiMessageArgs;
    protected String apiMessage;
    protected String path;
    private List<String> causes;
    private Long timestamp = Instant.now().toEpochMilli();

    public JustAPICheckedException() {
    }

    public JustAPICheckedException(String message) {
        super(message);
    }

    public JustAPICheckedException(String message, String localizedMessage) {
        super(message);
    }

    public JustAPICheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustAPICheckedException(Throwable cause) {
        super(cause);
    }

    public JustAPICheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract int getHttpStatus();

    public abstract String getCode();

    public final String getExceptionClassName() {
        return this.getClass().getName();
    }

    public Object[] getApiMessageArgs() {
        return apiMessageArgs;
    }

    public void setApiMessageArgs(Object... apiMessageArgs) {
        this.apiMessageArgs = apiMessageArgs;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public String getPath() {
        return path;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public void addCause(String cause) {
        if (this.causes == null) {
            this.causes = new ArrayList<>();
        }
        this.causes.add(cause);
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
