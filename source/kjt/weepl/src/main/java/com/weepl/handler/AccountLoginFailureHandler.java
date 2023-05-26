package com.weepl.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class AccountLoginFailureHandler implements AuthenticationFailureHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            redirectStrategy.sendRedirect(request, response, "/members/login/error?cause=failed");
        } else if (exception instanceof LockedException) {
            redirectStrategy.sendRedirect(request, response, "/members/login/error?cause=locked");
        } else if (exception instanceof DisabledException) {
            redirectStrategy.sendRedirect(request, response, "/members/login/error?cause=restricted");
        } else {
            redirectStrategy.sendRedirect(request, response, "/members/login/error");
        }
    }
}