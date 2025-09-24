package com.kalutwarro.adventure_ai.controller;

import com.kalutwarro.adventure_ai.model.AdventureRequest;
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

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("adventureRequest", new AdventureRequest());
        return "index";
    }

    @PostMapping("/start")
    public String startAdventure(@ModelAttribute @Valid AdventureRequest adventureRequest, Model model) {
        String story = adventureService.startAdventure(adventureRequest);
        model.addAttribute("story", story);
        return "scene";
    }
}
