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

package pers.ykkz000.yukikaze.cef;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.slf4j.bridge.SLF4JBridgeHandler;
import pers.ykkz000.yukikaze.cef.api.DefaultRequestErrorHandler;
import pers.ykkz000.yukikaze.cef.configuration.CefConfiguration;
import pers.ykkz000.yukikaze.cef.handler.CefMessageRouterHandlerImpl;
import pers.ykkz000.yukikaze.framework.ApplicationContext;
import pers.ykkz000.yukikaze.framework.api.ModuleStarter;
import pers.ykkz000.yukikaze.framework.api.annotation.DefineAnnotationProcessors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@DefineAnnotationProcessors(CefAnnotationProcessor.class)
public class CefStarter implements ModuleStarter {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
    @Override
    public void start(ApplicationContext context) {
        // Start up the CEF app
        CefApp.startup(context.getArgs());
        CefAppBuilder cefAppBuilder = new CefAppBuilder();
        cefAppBuilder.addJcefArgs("--disable-web-security");
        cefAppBuilder.addJcefArgs("--disable-site-isolation-trials");
        cefAppBuilder.getCefSettings().windowless_rendering_enabled = OS.isLinux();
        try {
            cefAppBuilder.build();
        } catch (IOException | UnsupportedPlatformException | InterruptedException | CefInitializationException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> CefApp.getInstance().dispose()));

        // Load properties
        CefConfiguration cefConfiguration = new CefConfiguration(context.getYukikazeProperties().child("cef"));

        // Create the CEF browser
        CefClient cefClient = CefApp.getInstance().createClient();
        cefClient.addMessageRouter(CefMessageRouter.create(new CefMessageRouterHandlerImpl(context, new DefaultRequestErrorHandler())));
        CefBrowser browser = cefClient.createBrowser(cefConfiguration.getUrl(), OS.isLinux(), false);

        // Create the UI frame
        JFrame frame = new JFrame();
        if (cefConfiguration.getFrame().getTitle() != null) {
            frame.setTitle(cefConfiguration.getFrame().getTitle());
        }
        if (cefConfiguration.getFrame().getIcon() != null) {
            try {
                frame.setIconImage(new ImageIcon(new URL(cefConfiguration.getFrame().getIcon())).getImage());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        frame.setSize(new Dimension(cefConfiguration.getFrame().getWidth(), cefConfiguration.getFrame().getHeight()));
        frame.setMinimumSize(new Dimension(cefConfiguration.getFrame().getMinWidth(), cefConfiguration.getFrame().getMinHeight()));
        frame.setMaximumSize(new Dimension(cefConfiguration.getFrame().getMaxWidth(), cefConfiguration.getFrame().getMaxHeight()));
        frame.setResizable(cefConfiguration.getFrame().isResizable());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(cefConfiguration.getFrame().getX(), cefConfiguration.getFrame().getY());
        frame.setLayout(new BorderLayout(0, 0));
        frame.add(browser.getUIComponent(), BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Associate handlers with the browser
        cefClient.addDisplayHandler(new CefDisplayHandlerAdapter() {
            @Override
            public void onTitleChange(CefBrowser browser, String title) {
                frame.setTitle(title);
            }
        });

        // Show the frame in the UI thread
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
