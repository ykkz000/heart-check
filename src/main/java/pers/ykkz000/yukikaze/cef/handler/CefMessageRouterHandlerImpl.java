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

package pers.ykkz000.yukikaze.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import pers.ykkz000.yukikaze.cef.api.RequestErrorHandler;
import pers.ykkz000.yukikaze.framework.ApplicationContext;
import pers.ykkz000.yukikaze.framework.exception.ResponseErrorException;

import java.util.Hashtable;
import java.util.Map;

public class CefMessageRouterHandlerImpl extends CefMessageRouterHandlerAdapter {
    private final ApplicationContext context;
    private final RequestErrorHandler requestErrorHandler;
    public CefMessageRouterHandlerImpl(ApplicationContext context, RequestErrorHandler requestErrorHandler) {
        this.context = context;
        this.requestErrorHandler = requestErrorHandler;
    }
    protected String route(String request) throws ResponseErrorException {
        String[] split = request.split("\\?");
        if (split.length != 2) {
            throw new ResponseErrorException(400, "Invalid request");
        }
        String command = split[0];
        String [] pairs = split[1].split("&");
        Map<String, String> args = new Hashtable<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length != 2) {
                throw new ResponseErrorException(400, "Invalid request");
            }
            args.put(keyValue[0], keyValue[1]);
        }
        return context.getCommandRouter().execute(command, args);
    }
    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        try {
            String result = route(request);
            callback.success(result);
            return true;
        } catch (ResponseErrorException e) {
            String result = requestErrorHandler.process(e, context, browser, frame, queryId, request, persistent);
            callback.failure(e.getStatus(), result);
            return true;
        }
    }
}
