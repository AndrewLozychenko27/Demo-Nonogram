package ua.lozychenko.nonogram.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import ua.lozychenko.nonogram.controller.GameController;
import ua.lozychenko.nonogram.service.data.GameService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @MockBean
    GameService gameService;

    @Test
    @WithMockUser(username = "player@gmail.com")
    void getLeaders_shouldReturnView_andModelWithLeaders_whenAuthorized() throws Exception {
        when(gameService.getLeaders()).thenReturn(new ArrayList<>());

        ModelAndView modelAndView = mvc.perform(get("/game/leaders"))
                .andExpect(status().isOk())
                .andReturn().getModelAndView();

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getViewName()).isEqualTo("game-leaders");
        assertThat(modelAndView.getModel().get("leaders")).isNotNull();
    }

    @Test
    void getLeaders_shouldReturn401HttpCode_whenUnauthorized() throws Exception {
        when(gameService.getLeaders()).thenReturn(new ArrayList<>());

        mvc.perform(get("/game/leaders"))
                .andExpect(status().isUnauthorized());
    }
}