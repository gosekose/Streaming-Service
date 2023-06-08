package com.server.streaming.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.server.streaming.common.principal.service.CustomOAuth2UserService;
import com.server.streaming.common.principal.service.CustomOidcUserService;
import com.server.streaming.controller.filter.AuthenticationGiveFilter;
import com.server.streaming.controller.filter.LoginSessionFilter;
import com.server.streaming.repository.redis.LoginSessionRepository;
import com.server.streaming.repository.redis.TokenRepository;
import com.server.streaming.service.tokenprovider.TokenProviderPolicyImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final CorsFilter corsFilter;

    private final TokenProviderPolicyImpl tokenProvider;
    private final TokenRepository tokenRepository;
    private final LoginSessionRepository loginSessionRepository;
    private final FilterRegistrationBean<LoginSessionFilter> loginSessionFilter;

    private static final String[] webServiceWhiteList = {
            "/", "/static/**", "/static/js/**", "/static/images/**",
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
                        .authenticated())

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
    public AuthenticationGiveFilter authenticationGiveFilter() { return new AuthenticationGiveFilter(tokenProvider, tokenRepository, loginSessionRepository);}

    @Bean
    Customizer<SessionManagementConfigurer<HttpSecurity>> sessionCustomizer() {
        return httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        return registrationId -> null;
    }
}

