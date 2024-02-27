package ua.lozychenko.nonogram.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.lozychenko.nonogram.service.data.GameService;

@Controller
@RequestMapping("/game")
public class GameController {
    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/leaders")
    public String leaders(Pageable pageable,
                          Model model) {
        model.addAttribute("leaders", gameService.getLeaders(pageable));

        return "game-leaders";
    }
}