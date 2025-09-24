package com.kalutwarro.adventure_ai.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdventureSessionModel {
    private String genre;
    private String mainCharacterName;
    private String mainCharacterDescription;
    private String location;
    private String duration; // corta, media, larga
    private String complexity; // baja, media, alta

    private int totalTurns; // según duración
    private int currentTurn = 0;

    private List<String> history = new ArrayList<>(); // cada fragmento de historia
    private List<String> choices = new ArrayList<>(); // decisiones tomadas
    private int energy = 100; // ejemplo de estado físico
    private int mood = 100;   // ejemplo de estado emocional

    public AdventureSessionModel(AdventureRequestModel req) {
        this.genre = req.getGenre();
        this.mainCharacterName = req.getMainCharacterName();
        this.mainCharacterDescription = req.getMainCharacterDescription();
        this.location = req.getLocation();
        this.duration = req.getDuration();
        this.complexity = req.getComplexity();
        switch (duration.toLowerCase()) {
            case "corta" -> totalTurns = 5;
            case "media" -> totalTurns = 10;
            case "larga" -> totalTurns = 20;
            default -> totalTurns = 5;
        }
    }

    public boolean isFinished() {
        return currentTurn >= totalTurns;
    }
}
