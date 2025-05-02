package com.question.learning_management_system.configuration;

import com.question.learning_management_system.configuration.jwt.AuthTokenFilter;
import com.question.learning_management_system.configuration.jwt.JwtAuthEntryPoint;
import com.question.learning_management_system.configuration.jwt.JwtUtils;
import com.question.learning_management_system.configuration.user.UserDetailsService.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@ComponentScan(basePackages = "com.question.learning_management_system")
public class SecurityConfiguration {

    private final CustomerUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final JwtAuthEntryPoint authEntryPoint;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authEntryPoint))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",       // السماح لـ OpenAPI Docs
                                "/swagger-ui/**",        // السماح لـ Swagger UI
                                "/swagger-ui.html"       // السماح بصفحة Swagger
                        ).permitAll()

                        // السماح للمستخدمين الموثوقين بالوصول إلى الـ Students
                        .requestMatchers("/api/students/**")
                        .hasAnyAuthority("USER", "ADMIN")

                        // السماح فقط للـ ADMIN بالوصول إلى الـ Instructors
                        .requestMatchers("/api/instructors/**")
                        .hasAuthority("ADMIN")


                        // السماح للمستخدمين بالوصول إلى التعليقات
                        .requestMatchers("/api/comments/**")
                        .hasAnyAuthority("USER", "ADMIN")  // يمكن لـ USER إضافة التعليقات و ADMIN إدارة التعليقات

                        // السماح للمستخدمين بالوصول إلى الاشتراكات (Enrollment)
                        .requestMatchers("/api/enrollments/**")
                        .hasAnyAuthority("USER", "ADMIN")  // يمكن لـ USER الاشتراك في الكورسات و ADMIN إدارة الاشتراكات

                        // السماح لبقية الـ Requests
                        .anyRequest().permitAll()
                );

        // تعيين الـ Authentication Provider
        http.authenticationProvider(authenticationProvider());

        // إضافة الـ Token Filter للتحقق من صحة الـ JWT
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

