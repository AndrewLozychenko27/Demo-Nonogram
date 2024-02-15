package ua.lozychenko.nonogram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.lozychenko.nonogram.data.entity.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                        requests
                                .antMatchers(HttpMethod.GET,
                                        "/static/**",
                                        "/",
                                        "/login",
                                        "/user/create")
                                .permitAll()
                                .antMatchers(HttpMethod.POST,
                                        "/login",
                                        "/user/create",
                                        "/game/{puzzle_id}/check",
                                        "/game/{puzzle_id}/save-state",
                                        "/game/{puzzle_id}/hint")
                                .permitAll()

                                .antMatchers(HttpMethod.GET,
                                        "/puzzle/list",
                                        "/puzzle/create",
                                        "/puzzle/{puzzle_id}/play",
                                        "/puzzle/{puzzle_id}/fill")
                                .authenticated()
                                .antMatchers(HttpMethod.POST,
                                        "/logout",
                                        "/puzzle/create",
                                        "/puzzle/{puzzle_id}/check")
                                .authenticated()

                                .antMatchers(HttpMethod.GET,
                                        "/user/{user_id}/profile",
                                        "/user/{user_id}/edit",
                                        "/user/{user_id}/reset-password",
                                        "/user/{user_id}/delete")
                                .access("@securityHelper.isSelfEdit(principal, #user_id)")
                                .antMatchers(HttpMethod.POST,
                                        "/user/{user_id}/edit",
                                        "/user/{user_id}/reset-password",
                                        "/user/{user_id}/delete")
                                .access("@securityHelper.isSelfEdit(principal, #user_id)")

                                .antMatchers(HttpMethod.GET,
                                        "/puzzle/{puzzle_id}/edit",
                                        "/puzzle/{puzzle_id}/delete")
                                .access("@securityHelper.isPuzzleOwner(principal, #puzzle_id)")
                                .antMatchers(HttpMethod.POST,
                                        "/puzzle/{puzzle_id}/edit",
                                        "/puzzle/{puzzle_id}/delete",
                                        "/puzzle/{puzzle_id}/fill")
                                .access("@securityHelper.isPuzzleOwner(principal, #puzzle_id)")

                                .antMatchers(HttpMethod.GET,
                                        "/user/list")
                                .hasAuthority(Role.ADMIN.getAuthority())
                                .antMatchers(HttpMethod.POST,
                                        "/user/change-role",
                                        "/user/change-status")
                                .hasAuthority(Role.ADMIN.getAuthority())

                                .anyRequest().denyAll())
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                                .usernameParameter("nickname")
                                .permitAll())
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}