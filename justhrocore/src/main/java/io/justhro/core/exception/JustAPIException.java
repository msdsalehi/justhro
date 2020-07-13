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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.justhro.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "detailArgs", "localizedMessage", "message"},
        ignoreUnknown = true)
public abstract class JustAPIException extends RuntimeException implements JustAPIExceptionDetailsProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(JustAPIException.class);
    static final String INTERNAL_CODE_PREFIX = "*-";
    protected String type;
    protected String title;
    private Object[] detailArgs;
    protected String detail;
    protected String instance;
    private List<String> causes;
    private Long timestamp = Instant.now().toEpochMilli();
    private String rootCauseCode;

    public JustAPIException(String message, Throwable cause, JustAPIException rootCause) {
        super(message, cause);
        try {
            if (rootCause != null) {
                String rootCauseCode = rootCause.getRootCauseCode();
                ReflectionUtil.setFieldValue(this, rootCauseCode == null ? rootCause.getCode() : rootCauseCode,
                        "rootCauseCode");
            }
        } catch (Exception ex) {
            LOGGER.error("Error while trying to set rootCause.", ex);
        }
    }

    public JustAPIException(Throwable cause, JustAPIException rootCause) {
        this(null, cause, rootCause);
    }

    public JustAPIException(String message, JustAPIException rootCause) {
        this(message, null, rootCause);
    }

    public JustAPIException(JustAPIException rootCause) {
        this(null, null, rootCause);
    }

    public JustAPIException() {
    }

    public final String getExceptionClassName() {
        return this.getClass().getName();
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Object[] getDetailArgs() {
        return detailArgs;
    }

    public void setDetailArgs(Object... detailArgs) {
        this.detailArgs = detailArgs;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
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

    public String getRootCauseCode() {
        return rootCauseCode == null ? getCode() : rootCauseCode;
    }
}
