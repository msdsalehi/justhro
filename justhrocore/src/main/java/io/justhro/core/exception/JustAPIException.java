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

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "apiMessageArgs"},
        ignoreUnknown = true)
public abstract class JustAPIException extends RuntimeException {

    static final String INTERNAL_CODE_PREFIX = "*-";
    private Object[] localizedMessageArgs;
    protected String serviceKey;
    private List<String> causes;

    public JustAPIException() {
    }

    public JustAPIException(String message) {
        super(message);
    }

    public JustAPIException(String message, String localizedMessage) {
        super(message);
    }

    public JustAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustAPIException(Throwable cause) {
        super(cause);
    }

    public JustAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract int getHttpStatus();

    public abstract String getCode();

    public Object[] getLocalizedMessageArgs() {
        return localizedMessageArgs;
    }

    public void setLocalizedMessageArgs(Object... localizedMessageArgs) {
        this.localizedMessageArgs = localizedMessageArgs;
    }

    public String getServiceKey() {
        return serviceKey;
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

    public final String getExceptionClassName() {
        return this.getClass().getName();
    }
}
