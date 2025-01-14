package hcmute.kltn.vtv.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.util.CustomAccessDeniedHandler;
import hcmute.kltn.vtv.util.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {



    private static final String[] NO_AUTH = {
            "/api/auth/**",
            "/api/product/**",
            "/api/search/**",
            "/api/shop/**",
            "/api/voucher/**",
            "/api/review/**",
            "/api/comment/**",
            "/api/chat/**",
            "/api/location/**",

            "/api/payment/**",
            "/api/category/**",
            "/api/brand/**",
            "/api/vendor/product/add/test",

            "/api/chat/room/**",
            "/api/chat/message/**",

            "/api/product-suggestion/**",

            "/api/customer/notification",
            "/api/product-filter/**",

            "/api/category-shop/**",
            "/api/vnpay/return/**",






            "/api/customer/reset-password",
            "/api/customer/active-account",
            "/api/customer/active-account/send-email",
            "/api/customer/forgot-password",

            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };






    private static final String[] CUSTOMER_ROLE = {
            "/api/customer/**",
            "/api/favorite-product/**",
            "/api/vendor/register/**",
            "/api/followed-shop/**",
            "/api/cart/**",
            "/api/order/**",
            "/api/vnpay/**",

            // "/api/payment/**",

    };

    private static final String[] VENDOR_ROLE = {
            "/api/vendor/**",
    };

    private static final String[] ADMIN_ROLE = {
            "/api/admin/category/**",
            "/api/admin/brand/**",
            "/api/manager/**",


    };

    private static final String[] MANAGER_ROLE = {
            "/api/manager/**",
            "/api/manager-info/get/manger"

    };

    private static final String[] MANAGER_CUSTOMER_ROLE = {
            "/api/manager/customer/**",
            "/api/manager-info/get/manger-customer"


    };

    private static final String[] MANAGER_VENDOR_ROLE = {

            "/api/manager/vendor/**",
            "/api/manager-info/get/manger-vendor"
    };

    private static final String[] MANAGER_SHIPPING_ROLE = {

            "/api/manager/deliver/**",
            "/api/manager-info/get/manger-shipping"
    };

    private static final String[] DELIVER_MANAGER_ROLE = {
            "/api/shipping/**"
    };

    private static final String[] DELIVER_ROLE = {
            "/api/shipping/deliver/**",
            "/api/shipping/transport/**",
            "/api/shipping/cash-order/**",
    };


    private static final String[] PROVIDER_ROLE = {
            "/api/shipping/**",
    };


    private static final String[] WEBSOCKET_ENDPOINT = {
            "/ws-stomp/**"
    };


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final ObjectMapper objectMapper;
    private final LogoutHandler logoutHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint; // Thêm trường này


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
//                .cors(cors -> cors.configurationSource(request -> {
//                    var corsConfiguration = new CorsConfiguration();
//                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:3000", "http://localhost:5173"));
//                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
//                    corsConfiguration.setAllowCredentials(true);
//                    return corsConfiguration;
//                }))

//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(STATELESS)
//                        .sessionFixation().migrateSession() // Change session ID on authentication
//                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers(NO_AUTH)
                        .permitAll()

                        .requestMatchers(CUSTOMER_ROLE)
                        .authenticated()

                        .requestMatchers(VENDOR_ROLE)
                        .hasRole(Role.VENDOR.name())

                        .requestMatchers(ADMIN_ROLE)
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(MANAGER_ROLE)
                        .hasRole(Role.MANAGER.name())

                        .requestMatchers(MANAGER_CUSTOMER_ROLE)
                        .hasRole(Role.MANAGERCUSTOMER.name())

                        .requestMatchers(MANAGER_VENDOR_ROLE)
                        .hasRole(Role.MANAGERVENDOR.name())

                        .requestMatchers(MANAGER_SHIPPING_ROLE)
                        .hasRole(Role.MANAGERSHIPPING.name())

                        .requestMatchers(DELIVER_ROLE)
                        .hasRole(Role.DELIVER.name())

                        .requestMatchers(PROVIDER_ROLE)
                        .hasRole(Role.PROVIDER.name())

                        .requestMatchers(DELIVER_MANAGER_ROLE)
                        .hasRole(Role.DELIVER_MANAGER.name())

                        .requestMatchers(WEBSOCKET_ENDPOINT)
                        .permitAll()


                        .anyRequest()
                        .authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler()))

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint) // Sử dụng AuthenticationEntryPoint tùy chỉnh
                );


        return http.build();
    }






    private AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }


}
