package com.kalutwarro.adventure_ai.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdventureRequest {
    @NotBlank
    private String genre; // género
    private int protagonists; // cantidad de protagonistas
    @NotBlank
    private String mainCharacterName;
    private String mainCharacterDescription;
    @NotBlank
    private String duration; // corta, media, larga
    @NotBlank
    private String complexity; // baja, media, alta
    @NotBlank
    private String location;
}
