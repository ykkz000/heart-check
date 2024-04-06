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

package pers.ykkz000.yukikaze.cef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import pers.ykkz000.yukikaze.cef.api.RequestErrorHandler;
import pers.ykkz000.yukikaze.framework.ApplicationContext;
import pers.ykkz000.yukikaze.framework.exception.ResponseErrorException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class DefaultRequestErrorHandler implements RequestErrorHandler {
    @Override
    public String process(ResponseErrorException e, ApplicationContext context, CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent) {
        String statusDescription = switch (e.getStatus()) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Request Entity Too Large";
            case 414 -> "Request-URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Requested Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 418 -> "I'm a teapot";
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Entity";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";
            default -> "Unknown Error";
        };
        ObjectMapper objectMapper = context.getObjectMapper();
        String exception = null;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        if (e.getCause() != null) {
            e.getCause().printStackTrace(printWriter);
            exception = stringWriter.toString();
        }
        printWriter.close();
        try {
            stringWriter.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            return objectMapper.writeValueAsString(ReturnJson.builder()
                    .status(e.getStatus())
                    .statusDescription(statusDescription)
                    .request(request)
                    .message(e.getMessage())
                    .exception(exception)
                    .build());
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Getter
    @Setter
    @Builder
    public static class ReturnJson {
        private int status;
        private String statusDescription;
        private String request;
        private String message;
        private String exception;
    }
}
