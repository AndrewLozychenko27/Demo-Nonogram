package ua.lozychenko.nonogram.controller.rest;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lozychenko.nonogram.data.entity.Puzzle;
import ua.lozychenko.nonogram.data.entity.User;
import ua.lozychenko.nonogram.service.data.CellService;
import ua.lozychenko.nonogram.service.data.GameService;

@RestController
@RequestMapping("/game")
public class GameRestController {
    private final GameService gameService;
    private final CellService cellService;

    public GameRestController(GameService gameService, CellService cellService) {
        this.gameService = gameService;
        this.cellService = cellService;
    }

    @PostMapping("/{puzzle_id}/check")
    public void check(@PathVariable("puzzle_id") Puzzle puzzle,
                      @AuthenticationPrincipal User user,
                      @RequestBody Cells cells) {
        gameService.check(puzzle, user, cellService.parseCells(cells.getCells()));
    }

    @PostMapping("/{puzzle_id}/save-state")
    public void saveState(@PathVariable("puzzle_id") Puzzle puzzle,
                          @AuthenticationPrincipal User user,
                          @RequestBody Cells cells) {
        gameService.save(puzzle, user, cellService.parseCells(cells.getCells()));
    }

    @PostMapping("/{puzzle_id}/hint")
    public void hint(@PathVariable("puzzle_id") Puzzle puzzle,
                     @AuthenticationPrincipal User user,
                     @RequestBody Cells cells) {
        gameService.save(puzzle, user, cellService.parseCells(cells.getCells()));
        gameService.giveHints(puzzle, user);
    }

    private static class Cells {
        private String[] cells;

        public Cells() {
        }

        public Cells(String[] cells) {
            this.cells = cells;
        }

        public String[] getCells() {
            return cells;
        }

        public void setCells(String[] cells) {
            this.cells = cells;
        }
    }
}