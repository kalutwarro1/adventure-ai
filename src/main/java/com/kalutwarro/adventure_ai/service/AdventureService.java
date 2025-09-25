package com.kalutwarro.adventure_ai.service;

import com.kalutwarro.adventure_ai.model.AdventureSessionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdventureService {

    private final ChatClient chatClient;

    @Autowired
    private ImageService imageService;

    public AdventureService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String startAdventure(AdventureSessionModel req) {
        PromptTemplate promptTemplate = new PromptTemplate("""
        Eres un narrador de aventuras.
        Crea el inicio de una historia del género {genre}, con protagonista llamado {name},
        ubicado en {location}.
        El protagonista tiene esta descripción: {description}.
        
        La duración será {duration} (5, 10 o 20 decisiones en total).
        La complejidad de cada turno será {complexity} (2, 3 o 5 opciones).
        Debes devolver SOLO el primer fragmento de la historia y las opciones de decisión iniciales.
        
        Recuerda incluir al automóvil Megalodón y al menos uno de sus features en la narrativa.
        """);

        // Seteo de variables al prompt
        Prompt propmt = promptTemplate.create(Map.of("genre", req.getGenre(), "name", req.getMainCharacterName(),
                        "description", req.getMainCharacterDescription(), "duration", req.getDuration(),
                        "complexity", req.getComplexity(), "location", req.getLocation()));

        String response = chatClient.prompt(propmt).call().chatResponse().getResult().getOutput().getText();

        // Generar imagen con Stable Diffusion
        String imgPrompt = "Ilustración épica de " + req.getMainCharacterName() + " en el auto Megalodón. Escena: " + response;
        String imageBase64 = imageService.generateImage(imgPrompt);
        // Setear Imagen
        req.setCurrentImage(imageBase64); // guardás la imagen en la sesión

        // ✅ Guarda el primer fragmento en el history
        req.getHistory().add(response);

        return response;
    }

    /**
     * Generar historia por turnos
     * @param session Objeto AdventureSession
     * @param chosenOption opción elegida por el usuario.
     * @return texto con la descripción de la historia. */
    public String nextTurn(AdventureSessionModel session, String chosenOption) {
        session.getChoices().add(chosenOption);
        session.setCurrentTurn(session.getCurrentTurn() + 1);

        // Estado físico/emocional random simple mood/energy
        session.setEnergy(session.getEnergy() - (int)(Math.random()*10));
        session.setMood(session.getMood() - (int)(Math.random()*5));

        String lastFragment = session.getHistory().isEmpty() ?
                "Inicio de la aventura." : session.getHistory().get(session.getHistory().size()-1);

        // Seleccionamos feature simple (RAG)
        String feature = "El Megalodón alcanza 320 km/h";

        // Prompt dinámico por turno
        String template = """
Eres un narrador de aventuras.
Estamos en el turno %s de %s.
La complejidad es %s (2,3 o 5 opciones).
Continúa la historia desde el último fragmento:
%s

Incluye siempre al Megalodón y al menos un feature del auto: %s.

Devuelve la respuesta con la escena seguida por una sección 'OPTIONS:' con las opciones, cada con un salto de línea \n.
""".formatted(
                session.getCurrentTurn(),
                session.getTotalTurns(),
                session.getComplexity(),
                lastFragment,
                feature
        );

        String jsonResponse = chatClient.prompt(template).call().chatResponse().getResult().getOutput().getText();

        // Generar imagen con Stable Diffusion
        String imgPrompt = "Ilustración épica de " + session.getMainCharacterName() + " en el auto Megalodón. Escena: " + jsonResponse;
        String imageBase64 = imageService.generateImage(imgPrompt);
        // Setear Imagen
        session.setCurrentImage(imageBase64); // guardás la imagen en la sesión

        session.getHistory().add(jsonResponse); // guardamos la historia completa

        return jsonResponse;
    }
}
