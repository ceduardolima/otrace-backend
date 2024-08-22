package com.celc.otrace.infra.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.celc.otrace.domain.User.Account;
import com.celc.otrace.repositories.AccountRepository;
import com.celc.otrace.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SecutiryFilter extends OncePerRequestFilter {
    final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var token = getToken(request);
        if (token != null) {
            final String email = tokenService.getSubject(token);
            final Account account = accountRepository.findByEmail(email).get();
            final var auth = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }


    private String getToken(HttpServletRequest request) {
        var rawToken = request.getHeader("authorization");
        if(rawToken != null) {
            return rawToken.replace("Bearer ", "");
        }
        return null;
    }
}
