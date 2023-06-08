package com.server.streaming.config;

import com.server.streaming.common.principal.service.CustomOAuth2UserService;
import com.server.streaming.common.principal.service.CustomOidcUserService;
import com.server.streaming.controller.filter.AuthenticationGiveFilter;
import com.server.streaming.controller.filter.LoginSessionFilter;
import com.server.streaming.service.tokenprovider.TokenProviderPolicyImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final CorsFilter corsFilter;
    private final TokenProviderPolicyImpl tokenProviderImpl;
    private final FilterRegistrationBean<LoginSessionFilter> loginSessionFilter;

    private static final String[] webServiceWhiteList = {
            "/static/**", "/static/js/**", "/static/images/**",
            "/static/css/**", "/static/scss/**", "/static/docs/**",
            "/h2-console/**", "/favicon.ico", "/error"
    };

    private static final String[] memberServiceWhiteList = {
            "/member-service/login",
            "/member-service/register"
    };


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(webServiceWhiteList);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(memberServiceWhiteList)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )

                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                        userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)))
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationGiveFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loginSessionFilter.getFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionCustomizer());

        return http.build();
    }

    @Bean
    public AuthenticationGiveFilter authenticationGiveFilter() { return new AuthenticationGiveFilter(tokenProviderImpl);}

    @Bean
    Customizer<SessionManagementConfigurer<HttpSecurity>> sessionCustomizer() {
        return httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}

