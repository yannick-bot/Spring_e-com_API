package com.ecommerce.sb_ecom.Security;

import com.ecommerce.sb_ecom.Model.AppRole;
import com.ecommerce.sb_ecom.Model.Role;
import com.ecommerce.sb_ecom.Model.User;
import com.ecommerce.sb_ecom.Repositories.RoleRepository;
import com.ecommerce.sb_ecom.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.sb_ecom.Security.Jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.Security.Jwt.AuthTokenFilter;
import com.ecommerce.sb_ecom.Security.CustomImpl.UserDetailsServiceImpl;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    /*
     VU qu'on utilise un UserDetailsService personnalisé
     on doit s'assurer qu'il est pris en compte par
     DaoAuthenticationProvider qui utilise UserDetailsService
     pour récupérer les détails de l'utilisateur et authentifier
     les informations d'identification de l'utilisateur par rapport à ces détails.
    */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                //.requestMatchers("/api/admin/**").permitAll()
                                //.requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));

        return http.build();
    }

    // Ce bean exclut les chemins spécifiés de l'influence
    // de security filter chain
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    @Transactional // Assure que les opérations sont exécutées dans une transaction
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Récupérer ou créer les rôles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newRole); // Persiste le rôle
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newRole); // Persiste le rôle
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newRole); // Persiste le rôle
                    });

            // Créer les utilisateurs s'ils n'existent pas déjà
            userRepository.findByUsername("user1").ifPresentOrElse(
                    user -> user.addRole(userRole), // Met à jour les rôles si l'utilisateur existe
                    () -> { // Crée l'utilisateur s'il n'existe pas
                        User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                        user1.addRole(userRole); // Associe le rôle
                        userRepository.save(user1); // Sauvegarde l'utilisateur
                    }
            );

            userRepository.findByUsername("seller1").ifPresentOrElse(
                    seller -> seller.addRole(sellerRole),
                    () -> {
                        User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                        seller1.addRole(sellerRole);
                        userRepository.save(seller1);
                    }
            );

            userRepository.findByUsername("admin").ifPresentOrElse(
                    admin -> {
                        admin.addRole(userRole);
                        admin.addRole(sellerRole);
                        admin.addRole(adminRole);
                    },
                    () -> {
                        User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                        admin.addRole(userRole);
                        admin.addRole(sellerRole);
                        admin.addRole(adminRole);
                        userRepository.save(admin);
                    }
            );
        };
    }
}