package ua.lozychenko.nonogram.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.lozychenko.nonogram.controller.OAuth2Controller;
import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.lozychenko.nonogram.constants.ControllerConstants.SESSION_USER;

@WebMvcTest(OAuth2Controller.class)
public class OAuth2ControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    private final OAuth2User mockUser = new DefaultOAuth2User(List.of(Role.PLAYER), Map.of("name", "testUser", "email", "testUser@test.org"), "name");

    @Test
    void onSuccess_shouldRedirect_andSetSessionAttribute_whenUserIsFoundByService() throws Exception {
        Optional<User> user = Optional.of(new User("testUser", "testUser@test.org", "qweQWE123", "qweQWE123"));
        user.get().setId(1L);

        when(userService.findByEmail(anyString())).thenReturn(user);

        MvcResult result = mvc.perform(get("/oauth/success").with(oauth2Login().oauth2User(mockUser)))
                .andExpect(status().is3xxRedirection())
                .andReturn();


        verify(userService, atLeastOnce()).findByEmail(anyString());
        verify(userService, never()).save(any(User.class));
        assertThat(result.getRequest().getSession()).isNotNull();
        assertThat(result.getRequest().getSession().getAttribute(SESSION_USER)).isNotNull();
    }

    @Test
    void onSuccess_shouldSetSessionAttributeAndSaveUser_whenUserIsNotFoundByService() throws Exception {
        User user = new User("testUser", "testUser@test.org", "qweQWE123", "qweQWE123");
        user.setId(1L);

        when(userService.save(any(User.class))).thenReturn(user);

        MvcResult result = mvc.perform(get("/oauth/success").with(oauth2Login().oauth2User(mockUser)))
                .andExpect(status().is3xxRedirection())
                .andReturn();


        verify(userService, atLeastOnce()).findByEmail(anyString());
        verify(userService, atLeastOnce()).save(any(User.class));
        assertThat(result.getRequest().getSession()).isNotNull();
        assertThat(result.getRequest().getSession().getAttribute(SESSION_USER)).isNotNull();
    }
}