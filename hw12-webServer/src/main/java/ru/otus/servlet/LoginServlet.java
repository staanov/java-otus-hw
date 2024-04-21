package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.services.AuthService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

public class LoginServlet extends HttpServlet {

  private static final String PARAM_LOGIN = "login";
  private static final String PARAM_PASSWORD = "password";
  private static final int MAX_INACTIVE_INTERVAL = 30;
  private static final String LOGIN_PAGE_TEMPLATE = "login.html";

  private final TemplateProcessor templateProcessor;
  private final AuthService authService;

  public LoginServlet(TemplateProcessor templateProcessor, AuthService authService) {
    this.templateProcessor = templateProcessor;
    this.authService = authService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    resp.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String name = req.getParameter(PARAM_LOGIN);
    String password = req.getParameter(PARAM_PASSWORD);

    if (authService.authenticate(name, password)) {
      HttpSession session = req.getSession();
      session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
      resp.sendRedirect("/clients");
    } else {
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
