package org.vaadin.example.appshell;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@Push
@Theme("my-theme")
@PWA(name = "Project Name", shortName = "Project Short Name")
public class AppShell implements AppShellConfigurator {
    // App shell configurations here
}
