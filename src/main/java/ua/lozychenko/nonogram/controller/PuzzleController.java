package ua.lozychenko.nonogram.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import ua.lozychenko.nonogram.constraint.util.ValidationHelper;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.data.service.CellService;
import ua.lozychenko.nonogram.data.service.GameService;
import ua.lozychenko.nonogram.data.service.PuzzleService;

import static ua.lozychenko.nonogram.constants.ControllerConstants.BINDING_RESULT;

@Controller
@RequestMapping("/puzzle")
public class PuzzleController {
    private final PuzzleService puzzleService;
    private final CellService cellService;
    private final GameService gameService;

    public PuzzleController(PuzzleService puzzleService, CellService cellService, GameService gameService) {
        this.puzzleService = puzzleService;
        this.cellService = cellService;
        this.gameService = gameService;
    }

    @ModelAttribute
    public Puzzle getPuzzle() {
        return new Puzzle();
    }

    @GetMapping("/list")
    public String puzzles(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                          Model model) {
        model.addAttribute("puzzles", puzzleService.findAll(pageable));

        return "puzzle-list";
    }

    @GetMapping("/create")
    public String createPuzzleForm() {
        return "puzzle-create";
    }

    @PostMapping("/create")
    public String createPuzzle(@Validated Puzzle puzzle,
                               BindingResult result,
                               @AuthenticationPrincipal User user,
                               Model model) {
        String view;

        if (result.hasErrors()) {
            model.addAttribute("puzzle", puzzle);
            model.addAttribute(BINDING_RESULT + "puzzle", ValidationHelper.filterErrors(result));
            view = "puzzle-create";
        } else {
            puzzle.setUser(user);
            puzzleService.save(puzzle);
            view = "redirect:/puzzle/list";
        }

        return view;
    }

    @GetMapping("/{puzzle_id}/play")
    public String play(@PathVariable("puzzle_id") Puzzle puzzle,
                       Model model) {
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("hints", puzzleService.generateHints(puzzle));

        return "puzzle-play";
    }

    @GetMapping("/{puzzle_id}/fill")
    public String fillForm(@PathVariable("puzzle_id") Puzzle puzzle,
                           Model model) {
        model.addAttribute("puzzle", puzzle);

        return "puzzle-fill";
    }

    @PostMapping("/{puzzle_id}/fill")
    public String fill(@PathVariable("puzzle_id") Puzzle puzzle,
                       @RequestParam(name = "cell", required = false) String[] coordinates,
                       Model model) {
        String view;

        if (coordinates == null) {
            model.addAttribute("error", "Puzzle must be filled!");
            model.addAttribute("puzzle", puzzle);
            view = "puzzle-fill";
        } else {
            puzzle.addCells(cellService.parseCells(coordinates));
            puzzleService.save(puzzle);
            view = "redirect:/puzzle/list";
        }

        return view;
    }

    @PostMapping("/{puzzle_id}/check")
    public String check(@PathVariable("puzzle_id") Puzzle puzzle,
                        @AuthenticationPrincipal User user,
                        @RequestParam(name = "cell") String[] coordinates) {
        gameService.check(puzzle, user, cellService.parseCells(coordinates));

        return String.format("redirect:/puzzle/%d/play", puzzle.getId());
    }
}