package com.kalutwarro.adventure_ai.controller;

import com.kalutwarro.adventure_ai.model.AdventureRequestModel;
import com.kalutwarro.adventure_ai.model.AdventureSessionModel;
import com.kalutwarro.adventure_ai.service.AdventureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aventura")
public class AdventureController {

    @Autowired
    private AdventureService adventureService;

    // Para simplificar usamos sesión en memoria
    private AdventureSessionModel currentSession;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("adventureRequestModel", new AdventureRequestModel());
        return "index";
    }

    @PostMapping("/start")
    public String startAdventure(@ModelAttribute AdventureRequestModel req, Model model) {
        currentSession = new AdventureSessionModel(req);
        String story = adventureService.startAdventure(currentSession); // ahora sí usamos startAdventure()
        model.addAttribute("story", story);
        return "scene";
    }

    @PostMapping("/next")
    public String nextTurn(@RequestParam String choice, Model model) {
        if (currentSession.isFinished()) {
            return "redirect:/aventura/end";
        }
        String story = adventureService.nextTurn(currentSession, choice);
        model.addAttribute("story", story);
        return "scene";
    }

    @GetMapping("/end")
    public String endAdventure(Model model) {
        // contemos cuántas veces aparece "Megalodón"
        int mentions = currentSession.getHistory().stream()
                .mapToInt(s -> s.split("Megalodón", -1).length - 1)
                .sum();
        model.addAttribute("history", currentSession.getHistory());
        model.addAttribute("mentions", mentions);
        return "end";
    }
}
