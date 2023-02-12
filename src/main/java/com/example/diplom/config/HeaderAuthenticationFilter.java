package com.example.diplom.config;

import com.example.diplom.service.UserService;
import com.example.diplom.text.Message;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
    private final UserService service;

    public HeaderAuthenticationFilter(UserService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/create")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader("auth-token");
        if (!service.checkJwt(jwt)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Message.INVALID_TOKEN);
            return;
        }
        Long id = service.getIdFromJWT(jwt);
        if (id == null)
            return;

        com.example.diplom.entity.User userFromDb = service.getUserById(id);
//        if (request.getRequestURI().equals("/logout")) {
//            userFromDb.setJwt("");
//            service.saveUser(userFromDb);
//            return;
//        }

        User user = new User(userFromDb.getLogin(), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
