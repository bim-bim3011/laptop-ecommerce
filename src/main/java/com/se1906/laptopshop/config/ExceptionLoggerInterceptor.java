package com.se1906.laptopshop.config;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.FileWriter;

@Component
public class ExceptionLoggerInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            try (PrintWriter out = new PrintWriter(new FileWriter("view-error.txt", true))) {
                ex.printStackTrace(out);
            }
        }
    }
}
