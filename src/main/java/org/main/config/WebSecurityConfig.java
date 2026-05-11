package org.main.config;

import org.main.services.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final AccessDeniedHandlerImpl DEFAULT_ACCESS_DENIED = new AccessDeniedHandlerImpl();

    @Autowired
    private UserSevice userSevice;

    @Autowired
    private AuthenticationSuccessHandler roleBasedLoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedLoginSuccessHandler() {
        return (request, response, authentication) -> {
            String ctx = request.getContextPath();
            boolean admin = authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
            boolean userRole = authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_USER".equals(a.getAuthority()));
            String target;
            if (admin) {
                target = ctx + "/admin?loginSuccess";
            } else if (userRole) {
                target = ctx + "/main?loginSuccess";
            } else {
                target = ctx + "/?loginSuccess";
            }
            response.sendRedirect(target);
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/registration", "/login", "/static/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/main", "/record/**").hasRole("USER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"))
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String path = request.getRequestURI().substring(request.getContextPath().length());
                    if (path.startsWith("/api")) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                    } else {
                        DEFAULT_ACCESS_DENIED.handle(request, response, accessDeniedException);
                    }
                })
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(roleBasedLoginSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSevice).passwordEncoder(passwordEncoder());
    }
}
