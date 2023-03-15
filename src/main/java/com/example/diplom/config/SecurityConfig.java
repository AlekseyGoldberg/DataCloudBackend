package com.example.diplom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final HeaderAuthenticationFilter headerAuthenticationFilter;
    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter, CustomLogoutHandler logoutHandler) {
        this.headerAuthenticationFilter = headerAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf()
                .disable()
                .x509()
                .and()
                .authorizeRequests().antMatchers("/create").permitAll()
                .and()
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("/logout").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .permitAll()
                .and()
                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
