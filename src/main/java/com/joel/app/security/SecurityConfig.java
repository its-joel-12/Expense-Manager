package com.joel.app.security;

import com.joel.app.model.AppRole;
import com.joel.app.model.Role;
import com.joel.app.model.User;
import com.joel.app.model.UserRole;
import com.joel.app.repository.RoleRepository;
import com.joel.app.repository.UserRepository;
import com.joel.app.repository.UserRoleRepository;
import com.joel.app.security.jwt.AuthEntryPointJwt;
import com.joel.app.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

//    @Autowired
//    @Lazy
//    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${initial.user.password}")
    private String userPassword;

    @Value("${initial.admin.password}")
    private String adminPassword;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((requests)
                -> requests
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/test").permitAll()
                .requestMatchers("/auth/public/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/users/**").permitAll()
                .anyRequest().authenticated());

//                .oauth2Login(oauth2 -> {
//                    oauth2.successHandler(oAuth2LoginSuccessHandler);
//                });

        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));

        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
                                      UserRoleRepository userRoleRepository) {
        return args -> {
            Role role = roleRepository.findByRoleName(AppRole.ROLE_USER.name())
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN.name())
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByEmail("user@example.com")) {
                User user = new User("User", "User", "user@example.com", passwordEncoder().encode(userPassword));
                user.setTwoFactorEnabled(false);
                user.setSignUpMethod("email");
                user.setCreatedBy("System");
                user = userRepository.save(user);
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(role.getRoleId());
                userRoleRepository.save(userRole);
            }

            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User("Admin", "Admin", "admin@example.com", passwordEncoder().encode(adminPassword));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setCreatedBy("System");
                admin = userRepository.save(admin);
                UserRole userRole = new UserRole();
                userRole.setUserId(admin.getUserId());
                userRole.setRoleId(adminRole.getRoleId());
                userRoleRepository.save(userRole);
            }
        };
    }
}
