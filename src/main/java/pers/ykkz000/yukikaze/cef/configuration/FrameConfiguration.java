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

@Getter
@Setter
public class FrameConfiguration {
    private int width;
    private int height;
    private int x;
    private int y;
    private int minWidth;
    private int minHeight;
    private int maxWidth;
    private int maxHeight;
    private boolean resizable;
    private String title;
    private String icon;

    public FrameConfiguration() {
        this.width = 1024;
        this.height = 768;
        this.x = 0;
        this.y = 0;
        this.minWidth = 1024;
        this.minHeight = 768;
        this.maxWidth = 1024;
        this.maxHeight = 768;
        this.resizable = true;
        this.title = null;
        this.icon = null;
    }

    public FrameConfiguration(@Nonnull YamlNode cefFrameNode) {
        this();
        if (cefFrameNode.value("width") != null) {
            this.width = Integer.parseInt(cefFrameNode.value("width"));
        }
        if (cefFrameNode.value("height") != null) {
            this.height = Integer.parseInt(cefFrameNode.value("height"));
        }
        if (cefFrameNode.value("x") != null) {
            this.x = Integer.parseInt(cefFrameNode.value("x"));
        }
        if (cefFrameNode.value("y") != null) {
            this.y = Integer.parseInt(cefFrameNode.value("y"));
        }
        if (cefFrameNode.value("min-width") != null) {
            this.minWidth = Integer.parseInt(cefFrameNode.value("min-width"));
        }
        if (cefFrameNode.value("min-height") != null) {
            this.minHeight = Integer.parseInt(cefFrameNode.value("min-height"));
        }
        if (cefFrameNode.value("max-width") != null) {
            this.maxWidth = Integer.parseInt(cefFrameNode.value("max-width"));
        }
        if (cefFrameNode.value("max-height") != null) {
            this.maxHeight = Integer.parseInt(cefFrameNode.value("max-height"));
        }
        if (cefFrameNode.value("resizable") != null) {
            this.resizable = Boolean.parseBoolean(cefFrameNode.value("resizable"));
        }
        if (cefFrameNode.value("title") != null) {
            this.title = cefFrameNode.value("title");
        }
        if (cefFrameNode.value("icon") != null) {
            setIcon(cefFrameNode.value("icon"));
        }
    }

    public void setIcon(String icon) {
        this.icon = URLUtil.filePathToUrl(icon);
    }
}
