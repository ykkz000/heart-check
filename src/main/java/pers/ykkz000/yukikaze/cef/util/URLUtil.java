/*
 * heart-check
 * Copyright (C) 2024  ykkz000
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

package pers.ykkz000.yukikaze.cef.util;

import java.io.File;
import java.util.regex.Pattern;

public class URLUtil {
    private static final Pattern FILE_PATTERN = Pattern.compile("^file\\(\".+\"\\)$");
    public static String filePathToUrl(String path) {
        if (path == null) {
            return null;
        } else if (!isFilePath(path)) {
            return path;
        } else {
            return new File(path.substring(6, path.length() - 2)).toURI().toString();
        }

    }
    public static boolean isFilePath(String rawUrl) {
        return rawUrl != null && FILE_PATTERN.matcher(rawUrl).matches();
    }
}
