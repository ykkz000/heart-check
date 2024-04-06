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

package pers.ykkz000.yukikaze.cef.configuration;

import lombok.Getter;
import lombok.Setter;
import pers.ykkz000.yukikaze.cef.util.URLUtil;
import pers.ykkz000.yukikaze.framework.util.YamlNode;

import javax.annotation.Nonnull;
import java.io.File;

@Getter
@Setter
public class CefConfiguration {
    private String url;
    private FrameConfiguration frame;
    public CefConfiguration() {
        this.url = "";
        this.frame = new FrameConfiguration();
    }

    public void setUrl(@Nonnull String url) {
        this.url = URLUtil.filePathToUrl(url);
    }

    public CefConfiguration(@Nonnull YamlNode cefNode) {
        this();
        if (cefNode.value("url") != null) {
            setUrl(cefNode.value("url"));
        }
        if (cefNode.child("frame") != null) {
            this.frame = new FrameConfiguration(cefNode.child("frame"));
        }
    }
}
