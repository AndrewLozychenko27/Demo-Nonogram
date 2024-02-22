package ua.lozychenko.nonogram.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import ua.lozychenko.nonogram.data.entity.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final ApplicationContext applicationContext;

    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(HttpMethod.GET,
                                        "/static/**",
                                        "/",
                                        "/login",
                                        "/user/create")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,
                                        "/login",
                                        "/user/create")
                                .permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        "/puzzle/list",
                                        "/puzzle/create",
                                        "/puzzle/{puzzle_id}/play",
                                        "/puzzle/{puzzle_id}/fill",
                                        "/oauth/success")
                                .authenticated()
                                .requestMatchers(HttpMethod.POST,
                                        "/logout",
                                        "/puzzle/create",
                                        "/puzzle/{puzzle_id}/check",
                                        "/game/{puzzle_id}/check",
                                        "/game/{puzzle_id}/save-state",
                                        "/game/{puzzle_id}/hint")
                                .authenticated()

                                .requestMatchers(HttpMethod.GET,
                                        "/user/{user_id}/profile",
                                        "/user/{user_id}/edit",
                                        "/user/{user_id}/reset-password",
                                        "/user/{user_id}/delete")
                                .access(processExpression("@securityHelper.isSelfEdit(principal, #user_id)"))

                                .requestMatchers(HttpMethod.POST,
                                        "/user/{user_id}/edit",
                                        "/user/{user_id}/reset-password",
                                        "/user/{user_id}/delete")
                                .access(processExpression("@securityHelper.isSelfEdit(principal, #user_id)"))

                                .requestMatchers(HttpMethod.GET,
                                        "/puzzle/{puzzle_id}/edit",
                                        "/puzzle/{puzzle_id}/delete")
                                .access(processExpression("@securityHelper.isPuzzleOwner(principal, #puzzle_id)"))
                                .requestMatchers(HttpMethod.POST,
                                        "/puzzle/{puzzle_id}/edit",
                                        "/puzzle/{puzzle_id}/delete",
                                        "/puzzle/{puzzle_id}/fill")
                                .access(processExpression("@securityHelper.isPuzzleOwner(principal, #puzzle_id)"))

                                .requestMatchers(HttpMethod.GET,
                                        "/user/list")
                                .hasAuthority(Role.ADMIN.getAuthority())
                                .requestMatchers(HttpMethod.POST,
                                        "/user/change-role",
                                        "/user/change-status")
                                .hasAuthority(Role.ADMIN.getAuthority())

                                .anyRequest().denyAll())
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .usernameParameter("nickname")
                                .permitAll())
                .oauth2Login(oauth ->
                        oauth
                                .loginPage("/login")
                                .defaultSuccessUrl("/oauth/success")
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll())
                .securityContext(context ->
                        context
                                .requireExplicitSave(false)
                                .securityContextRepository(new HttpSessionSecurityContextRepository()));

        return http.build();
    }

    private WebExpressionAuthorizationManager processExpression(final String expression) {
        final var expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        final var authorizationManager = new WebExpressionAuthorizationManager(expression);
        authorizationManager.setExpressionHandler(expressionHandler);
        return authorizationManager;
    }
}