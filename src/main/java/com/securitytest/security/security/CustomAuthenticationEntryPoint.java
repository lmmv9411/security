package com.securitytest.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        redirect(request, response, authException.getMessage());
    }

    private void redirect(HttpServletRequest request,
                          HttpServletResponse response,
                          String message) throws IOException, ServletException {

        var method = request.getMethod();

        var isApiRequest = method.equals("GET") || method.equals("HEAD");

        if (!isApiRequest) {
            final var mapper = new ObjectMapper();
            final var error = mapper.createObjectNode()
                    .put("code", HttpServletResponse.SC_UNAUTHORIZED)
                    .put("message", message)
                    .put("timestamp", LocalDateTime.now().toString())
                    .put("path", request.getRequestURI())
                    .put("method", request.getMethod());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writer().writeValue(response.getWriter(), error);
        } else {
            response.sendRedirect("/login");
        }
    }

}
