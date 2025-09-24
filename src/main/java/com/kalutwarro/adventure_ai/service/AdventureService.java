package com.kalutwarro.adventure_ai.service;

import com.kalutwarro.adventure_ai.model.AdventureSessionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdventureService {

    private final ChatClient chatClient;

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

        return chatClient.prompt(propmt).call().chatResponse().getResult().getOutput().getText();
    }

    /**
     * Generar historia por turnos
     * @param session Objeto AdventureSession
     * @param chosenOption opción elegida por el usuario.
     * @return texto con la descripción de la historia. */
    public String nextTurn(AdventureSessionModel session, String chosenOption) {
        session.getChoices().add(chosenOption);
        session.setCurrentTurn(session.getCurrentTurn() + 1);

        // Estado físico/emocional random simple
        session.setEnergy(session.getEnergy() - (int)(Math.random()*10));
        session.setMood(session.getMood() - (int)(Math.random()*5));

        // Prompt dinámico por turno
        String template = """
        Eres un narrador de aventuras.
        Estamos en el turno {turn} de {totalTurns}.
        La complejidad es {complexity} (2,3 o 5 opciones).
        Continúa la historia desde el último fragmento:
        {lastFragment}
        
        Incluye siempre al Megalodón y al menos un feature del auto: {feature}.
        Devuelve la respuesta en JSON:
        {
            "story": "...",
            "options": ["opción1","opción2",...]
        }
        """;

        String lastFragment = session.getHistory().isEmpty() ?
                "Inicio de la aventura." : session.getHistory().get(session.getHistory().size()-1);

        // Seleccionamos feature simple (RAG)
        String feature = "El Megalodón alcanza 320 km/h";

        PromptTemplate prompt = new PromptTemplate(template);
        prompt.add("turn", String.valueOf(session.getCurrentTurn()));
        prompt.add("totalTurns", String.valueOf(session.getTotalTurns()));
        prompt.add("complexity", session.getComplexity());
        prompt.add("lastFragment", lastFragment);
        prompt.add("feature", feature);

        String jsonResponse = chatClient.prompt(prompt.create()).call().content();
        session.getHistory().add(jsonResponse); // guardamos la historia completa

        return jsonResponse;
    }
}
