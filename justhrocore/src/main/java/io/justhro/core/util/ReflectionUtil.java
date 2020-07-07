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
package io.justhro.core.util;

import io.justhro.core.exception.JustAPICheckedException;
import io.justhro.core.exception.JustAPIException;

import java.lang.reflect.Field;

public final class ReflectionUtil {

    public static void setFieldValue(Exception instance, String localizedMessage, String apiMessage)
            throws NoSuchFieldException, IllegalAccessException {
        Field apiMessageField;
        if (instance instanceof JustAPICheckedException) {
            apiMessageField = JustAPICheckedException.class.getDeclaredField(apiMessage);
        } else {
            apiMessageField = JustAPIException.class.getDeclaredField(apiMessage);
        }
        apiMessageField.setAccessible(true);
        apiMessageField.set(instance, localizedMessage);
    }
}
