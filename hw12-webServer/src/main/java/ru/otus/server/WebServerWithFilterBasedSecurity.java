package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.AuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.AuthenticationFilter;
import ru.otus.servlet.LoginServlet;

import java.util.Arrays;

public class WebServerWithFilterBasedSecurity extends WebServerSimple {

  private final AuthService authService;

  public WebServerWithFilterBasedSecurity(int port, DBServiceClient serviceClient, TemplateProcessor templateProcessor, AuthService authService) {
    super(port, serviceClient, templateProcessor);
    this.authService = authService;
  }

  @Override
  protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
    servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
    var authFilter = new AuthenticationFilter();
    Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authFilter), path, null));
    return servletContextHandler;
  }
}
