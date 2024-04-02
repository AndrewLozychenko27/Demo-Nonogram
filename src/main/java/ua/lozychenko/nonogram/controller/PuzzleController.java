package ua.lozychenko.nonogram.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.lozychenko.nonogram.config.property.PuzzlePageProperty;
import ua.lozychenko.nonogram.constraint.group.NameGroup;
import ua.lozychenko.nonogram.constraint.group.SizeGroup;
import ua.lozychenko.nonogram.constraint.util.ValidationHelper;
import ua.lozychenko.nonogram.controller.util.ControllerHelper;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.data.GameService;
import ua.lozychenko.nonogram.service.data.PuzzleService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ua.lozychenko.nonogram.constants.ControllerConstants.BINDING_RESULT;

@Controller
@RequestMapping("/puzzle")
public class PuzzleController {
    public static final String RANDOM = "random";
    public static final String IMAGE = "image";
    private final PuzzlePageProperty properties;
    private final PuzzleService puzzleService;
    private final CellService cellService;
    private final GameService gameService;

    public PuzzleController(PuzzlePageProperty properties, PuzzleService puzzleService, CellService cellService, GameService gameService) {
        this.properties = properties;
        this.puzzleService = puzzleService;
        this.cellService = cellService;
        this.gameService = gameService;
    }

    @ModelAttribute
    public Puzzle getPuzzle() {
        return new Puzzle();
    }

    @GetMapping("/list")
    public String puzzles(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                          HttpSession session,
                          Model model) {
        User user = ControllerHelper.getCurrentUser(session);
        Pageable configuredPageable = pageable;

        if (Arrays.stream(properties.sizeRange()).noneMatch(size -> size == pageable.getPageSize())) {
            configuredPageable = PageRequest.of(pageable.getPageNumber(), properties.defaultSize(), pageable.getSort());
        }
        model.addAttribute("sizes", properties.sizeRange());
        model.addAttribute("puzzles", puzzleService.findAll(user.getId(), configuredPageable));

        return "puzzle-list";
    }

    @GetMapping("/create")
    public String createPuzzleForm() {
        return "puzzle-create";
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPuzzle(@Validated({NameGroup.class, SizeGroup.class}) Puzzle puzzle,
                               String fill,
                               @RequestParam(required = false) MultipartFile image,
                               BindingResult result,
                               HttpSession session,
                               Model model) throws IOException {
        String view = "puzzle-create";
        User user = ControllerHelper.getCurrentUser(session);

        if (result.hasErrors()) {
            model.addAttribute(BINDING_RESULT + "puzzle", ValidationHelper.filterErrors(result));
        } else {
            puzzle.setUser(user);
            if (RANDOM.equals(fill)) {
                session.setAttribute("generatedPuzzle", puzzleService.generatePuzzleRandomly(puzzle));
                view = "puzzle-generated-random";
            } else if (IMAGE.equals(fill)) {
                session.setAttribute("generatedPuzzles", puzzleService.generatePuzzlesByImage(puzzle, image.getBytes()));
                view = "puzzle-generated-image";
            } else {
                session.setAttribute("emptyPuzzle", puzzle);
                view = "puzzle-fill";
            }
        }

        return view;
    }

    @PostMapping("/fill")
    public String fill(@RequestParam(name = "cell", required = false) String[] coordinates,
                       HttpSession session,
                       Model model) {
        String view;
        Puzzle puzzle = (Puzzle) session.getAttribute("emptyPuzzle");

        if (coordinates == null) {
            model.addAttribute("error", "Puzzle must be filled!");
            view = "puzzle-fill";
        } else {
            puzzle.addCells(cellService.parseCells(coordinates));
            puzzle.setVisible(true);
            puzzleService.save(puzzle);
            view = "redirect:/puzzle/list";
            session.removeAttribute("emptyPuzzle");
        }

        return view;
    }

    @PostMapping("/save/image")
    public String savePuzzleImage(Integer puzzleId,
                                  HttpSession session) {
        List<Puzzle> puzzles = (List<Puzzle>) session.getAttribute("generatedPuzzles");
        puzzleService.save(puzzles.get(puzzleId));
        session.removeAttribute("generatedPuzzles");

        return "redirect:/puzzle/list";
    }

    @PostMapping("/save/random")
    public String savePuzzleRandom(HttpSession session) {
        Puzzle puzzle = (Puzzle) session.getAttribute("generatedPuzzle");
        puzzleService.save(puzzle);
        session.removeAttribute("generatedPuzzle");

        return "redirect:/puzzle/list";
    }

    @GetMapping("/{puzzle_id}/view")
    public String editForm(@PathVariable("puzzle_id") Puzzle puzzle,
                           Model model) {
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("puzzleStats", gameService.getPuzzleStats(puzzle));
        model.addAttribute("keys", puzzleService.generateKeys(puzzle));

        return "puzzle-view";
    }

    @GetMapping("/{puzzle_id}/play")
    public String play(@PathVariable("puzzle_id") Puzzle puzzle,
                       Model model) {
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("keys", puzzleService.generateKeys(puzzle));

        return "puzzle-play";
    }

    @GetMapping("/{puzzle_id}/delete")
    public String deleteForm(@PathVariable("puzzle_id") Puzzle puzzle,
                             Model model) {
        model.addAttribute("puzzle", puzzle);

        return "puzzle-delete";
    }

    @PostMapping("/{puzzle_id}/delete")
    public String delete(@PathVariable("puzzle_id") Puzzle puzzle,
                         String confirmation,
                         Model model) {
        String view;

        if (puzzle.getName().equals(confirmation)) {
            puzzleService.delete(puzzle.getId());
            view = "redirect:/puzzle/list";
        } else {
            model.addAttribute("puzzle", puzzle);
            model.addAttribute("error", "Puzzle wasn't deleted - confirmation failed");
            view = "puzzle-delete";
        }

        return view;
    }
}