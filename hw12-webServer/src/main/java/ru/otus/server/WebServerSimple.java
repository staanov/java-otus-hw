package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientsServlet;

public class WebServerSimple implements WebServer {

  private static final String START_PAGE_NAME = "index.html";
  private static final String COMMON_RESOURCE_DIR = "static";

  private final Server server;
  private final DBServiceClient serviceClient;
  protected final TemplateProcessor templateProcessor;

  public WebServerSimple(int port, DBServiceClient serviceClient, TemplateProcessor templateProcessor) {
    server = new Server(port);
    this.serviceClient = serviceClient;
    this.templateProcessor = templateProcessor;
  }

  @Override
  public void start() throws Exception {
    if (server.getHandlers().isEmpty()) {
      initContext();
    }
    server.start();
  }

  @Override
  public void join() throws Exception {
    server.join();
  }

  @Override
  public void stop() throws Exception {
    server.stop();
  }

  private void initContext() {
    var resourceHandler = createResourceHandler();
    var servletContextHandler = createServletContextHandler();

    Handler.Sequence sequence = new Handler.Sequence();
    sequence.addHandler(resourceHandler);
    sequence.addHandler(applySecurity(servletContextHandler, "/clients"));

    server.setHandler(sequence);
  }

  protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
    return servletContextHandler;
  }

  private ResourceHandler createResourceHandler() {
    var resourceHandler = new ResourceHandler();
    resourceHandler.setDirAllowed(false);
    resourceHandler.setWelcomeFiles(START_PAGE_NAME);
    resourceHandler.setBaseResourceAsString(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCE_DIR));
    return resourceHandler;
  }

  private ServletContextHandler createServletContextHandler() {
    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(serviceClient, templateProcessor)), "/clients");
    return servletContextHandler;
  }
}
