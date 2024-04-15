package ua.lozychenko.nonogram.unit;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;
import ua.lozychenko.nonogram.config.property.PuzzlePageProperty;
import ua.lozychenko.nonogram.controller.PuzzleController;
import ua.lozychenko.nonogram.controller.util.ControllerHelper;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.data.GameService;
import ua.lozychenko.nonogram.service.data.PuzzleService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PuzzleController.class)
public class PuzzleControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @Autowired
    PuzzlePageProperty properties;

    @MockBean
    PuzzleService puzzleService;

    @MockBean
    CellService cellService;

    @MockBean
    GameService gameService;

    @Test
    @WithMockUser(username = "player@gmail.com")
    void list_shouldReturnPuzzles_andPageSizes_whenPassedCorrectPageSize() throws Exception {
        User user = new User("player", "player@gmail.com", "", "");
        user.setId(1L);
        ModelAndView modelAndView;
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(puzzleService.findAll(anyLong(), any())).thenReturn(Page.empty());
        try (MockedStatic<ControllerHelper> mockControllerHelper = Mockito.mockStatic(ControllerHelper.class)) {
            mockControllerHelper.when(() -> ControllerHelper.getCurrentUser(any())).thenReturn(user);
            modelAndView = mvc.perform(get("/puzzle/list?size=23&page=2"))
                    .andExpect(status().isOk())
                    .andReturn().getModelAndView();
        }

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getViewName()).isEqualTo("puzzle-list");
        assertThat(modelAndView.getModel().get("sizes")).isNotNull();
        assertThat(modelAndView.getModel().get("puzzles")).isNotNull();
        verify(puzzleService).findAll(anyLong(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(23);
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void list_shouldReturnPuzzlesWithDefaultPageSize_andPageSizes_whenPassedWrongPageSize() throws Exception {
        User user = new User("player", "player@gmail.com", "", "");
        user.setId(1L);
        ModelAndView modelAndView;
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(puzzleService.findAll(anyLong(), any())).thenReturn(Page.empty());
        try (MockedStatic<ControllerHelper> mockControllerHelper = Mockito.mockStatic(ControllerHelper.class)) {
            mockControllerHelper.when(() -> ControllerHelper.getCurrentUser(any())).thenReturn(user);
            modelAndView = mvc.perform(get("/puzzle/list?size=17&page=2"))
                    .andExpect(status().isOk())
                    .andReturn().getModelAndView();
        }

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getViewName()).isEqualTo("puzzle-list");
        assertThat(modelAndView.getModel().get("sizes")).isNotNull();
        assertThat(modelAndView.getModel().get("puzzles")).isNotNull();
        verify(puzzleService).findAll(anyLong(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(properties.defaultSize());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnNameValidationError_whenNameIsNull() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "")
                                .queryParam("width", "10")
                                .queryParam("height", "10")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Puzzle name is required");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnNameValidationError_whenNameIsEmpty() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "      ")
                                .queryParam("width", "10")
                                .queryParam("height", "10")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Puzzle name is required");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnNameValidationError_whenNameIsTooLong() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890")
                                .queryParam("width", "10")
                                .queryParam("height", "10")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Puzzle name must be no longer than 256 characters");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnNameValidationError_whenNameContainsForbiddenChars() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "!@#$%^&*(ASDAS   asD123")
                                .queryParam("width", "10")
                                .queryParam("height", "10")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Puzzle name must contain only a-z, A-Z, 0-9 and _");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnSizeValidationError_whenSizeIsNull() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "Correct_Puzzle_Name_123")
                                .queryParam("width", "")
                                .queryParam("height", "")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Width is required");
        assertThat(result.getResolvedException().getMessage()).contains("Height is required");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnSizeValidationError_whenSizeIsLessThanMin() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "Correct_Puzzle_Name_123")
                                .queryParam("width", "-10")
                                .queryParam("height", "-10")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Width must be at least 10");
        assertThat(result.getResolvedException().getMessage()).contains("Height must be at least 10");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnSizeValidationError_whenSizeIsGreaterThanMax() throws Exception {
        MvcResult result = mvc.perform(
                        multipart("/puzzle/create")
                                .with(csrf())
                                .queryParam("name", "Correct_Puzzle_Name_123")
                                .queryParam("width", "50")
                                .queryParam("height", "50")
                                .queryParam("fill", "manual")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException()).isNotNull();
        assertThat(result.getResolvedException().getMessage()).contains("Width must be no longer than 40");
        assertThat(result.getResolvedException().getMessage()).contains("Height must be no longer than 40");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnPuzzleVerificationView_andSetSessionAttribute_whenOptionIsRandom_andValidationPassed() throws Exception {
        MvcResult result;
        User user = new User("testUser", "testUser@test.org", "qweQWE123", "qweQWE123");
        user.setId(1L);

        try (MockedStatic<ControllerHelper> mockControllerHelper = Mockito.mockStatic(ControllerHelper.class)) {
            mockControllerHelper.when(() -> ControllerHelper.getCurrentUser(any())).thenReturn(user);
            result = mvc.perform(
                            multipart("/puzzle/create")
                                    .with(csrf())
                                    .queryParam("name", "Correct_Puzzle_Name_123")
                                    .queryParam("width", "10")
                                    .queryParam("height", "10")
                                    .queryParam("fill", "random")
                    )
                    .andExpect(status().isOk())
                    .andReturn();
        }

        verify(puzzleService, atLeastOnce()).generatePuzzleRandomly(any());
        assertThat(result.getModelAndView()).isNotNull();
        assertThat(result.getModelAndView().getViewName()).isEqualTo("puzzle-generated-random");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnPuzzleVerificationView_andSetSessionAttribute_whenOptionIsImage_andValidationPassed() throws Exception {
        MvcResult result;
        User user = new User("testUser", "testUser@test.org", "qweQWE123", "qweQWE123");
        user.setId(1L);

        MockMultipartFile multipart = new MockMultipartFile("image", "puzzle.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "".getBytes());

        try (MockedStatic<ControllerHelper> mockControllerHelper = Mockito.mockStatic(ControllerHelper.class)) {
            mockControllerHelper.when(() -> ControllerHelper.getCurrentUser(any())).thenReturn(user);
            result = mvc.perform(
                            multipart("/puzzle/create")
                                    .file(multipart)
                                    .with(csrf())
                                    .queryParam("name", "Correct_Puzzle_Name_123")
                                    .queryParam("width", "10")
                                    .queryParam("height", "10")
                                    .queryParam("fill", "image")
                    )
                    .andExpect(status().isOk())
                    .andReturn();
        }

        verify(puzzleService, atLeastOnce()).generatePuzzlesByImage(any(), any());
        assertThat(result.getModelAndView()).isNotNull();
        assertThat(result.getModelAndView().getViewName()).isEqualTo("puzzle-generated-image");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void create_shouldReturnPuzzleFillView_andSetSessionAttribute_whenOptionIsManual_andValidationIsPassed() throws Exception {
        MvcResult result;
        User user = new User("testUser", "testUser@test.org", "qweQWE123", "qweQWE123");
        user.setId(1L);

        try (MockedStatic<ControllerHelper> mockControllerHelper = Mockito.mockStatic(ControllerHelper.class)) {
            mockControllerHelper.when(() -> ControllerHelper.getCurrentUser(any())).thenReturn(user);
            result = mvc.perform(
                            multipart("/puzzle/create")
                                    .with(csrf())
                                    .queryParam("name", "Correct_Puzzle_Name_123")
                                    .queryParam("width", "10")
                                    .queryParam("height", "10")
                                    .queryParam("fill", "manual")
                    )
                    .andExpect(status().isOk())
                    .andReturn();
        }

        assertThat(result.getResolvedException()).isNull();
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void fill_shouldReturnError_whenCoordinatesIsNull() throws Exception {
        MvcResult result = mvc.perform(
                        post("/puzzle/fill")
                                .with(csrf())
                                .sessionAttr("emptyPuzzle", new Puzzle())
                )
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getModelAndView()).isNotNull();
        assertThat(result.getModelAndView().getModel().get("error")).isEqualTo("Puzzle must be filled!");
        assertThat(result.getModelAndView().getViewName()).isEqualTo("puzzle-fill");
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void fill_shouldRedirect_andProcessPuzzle_whenCoordinatesIsSet() throws Exception {
        MvcResult result = mvc.perform(
                        post("/puzzle/fill")
                                .with(csrf())
                                .queryParam("cell", "1:1", "0:2")
                                .sessionAttr("emptyPuzzle", new Puzzle())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(cellService, atLeastOnce()).parseCells(any());
        verify(puzzleService, atLeastOnce()).save(any());
        assertThat(result.getRequest().getSession()).isNotNull();
        assertThat(result.getRequest().getSession().getAttribute("emptyPuzzle")).isNull();
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void saveImage_shouldRedirect_andSaveGeneratedPuzzle_andRemoveSessionAttribute_whenRequested() throws Exception {
        MvcResult result = mvc.perform(
                        post("/puzzle/save/image")
                                .with(csrf())
                                .queryParam("puzzleId", "0")
                                .sessionAttr("generatedPuzzles", List.of(new Puzzle()))
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(puzzleService, atLeastOnce()).save(any());
        assertThat(result.getRequest().getSession()).isNotNull();
        assertThat(result.getRequest().getSession().getAttribute("generatedPuzzles")).isNull();
    }

    @Test
    @WithMockUser(username = "player@gmail.com")
    void saveRandom_shouldRedirect_andSaveGeneratedPuzzle_andRemoveSessionAttribute_whenRequested() throws Exception {
        MvcResult result = mvc.perform(
                        post("/puzzle/save/random")
                                .with(csrf())
                                .sessionAttr("generatedPuzzle", new Puzzle())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(puzzleService, atLeastOnce()).save(any());
        assertThat(result.getRequest().getSession()).isNotNull();
        assertThat(result.getRequest().getSession().getAttribute("generatedPuzzle")).isNull();
    }
}