package holiday.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConf {

    // private final UserDetailsService userService;

    // public SecurityConf(UserDetailsService userService) {
    //     this.userService = userService;
    // }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/css/**", "/js/**", "/images/**", "/pics/**", "/postdata/**").permitAll()
                .requestMatchers("/activation/**", "/changepassword", "/setnewpassword").permitAll()
                .requestMatchers("/error", "calendar/**").permitAll()
                .requestMatchers("/adminuserupdatereg", "/userhandling").hasAnyAuthority("HR", "ADMIN", "USER")
                .requestMatchers("/new_userevent", "/newusereventreg").hasAnyAuthority("USER", "ADMIN", "HR")
                .requestMatchers("/users/**", "/registration", "/reg", "/userInfoPage/**").hasAnyAuthority("ADMIN", "HR")
                .requestMatchers("/events/**").hasAuthority("HR")
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
                .loginPage("/login").permitAll()
        )
        .rememberMe(rememberMe -> rememberMe
                .key("AbbGhYffDEr{_123Ju")
        )
        .logout(logout -> logout
                .logoutSuccessUrl("/login?logout").permitAll()
        )
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {});

        http.headers(headers -> headers
                .defaultsDisabled()
                .cacheControl(cache -> cache.disable())
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
