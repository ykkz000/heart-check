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

package pers.ykkz000.heartcheck;

import pers.ykkz000.yukikaze.cef.CefStarter;
import pers.ykkz000.yukikaze.framework.YukikazeApplication;
import pers.ykkz000.yukikaze.framework.annotation.EnableModule;
import pers.ykkz000.yukikaze.framework.annotation.LoadProperties;

@LoadProperties(path = "classpath:application.yaml")
@EnableModule(CefStarter.class)
public class MainApplication {

    public static void main(String[] args) {
        YukikazeApplication.run(MainApplication.class, args);
    }
}
