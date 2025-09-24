package com.kalutwarro.adventure_ai.service;

import com.kalutwarro.adventure_ai.model.AdventureRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdventureService {

    private final ChatClient chatClient;

    public AdventureService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String startAdventure(AdventureRequest req) {
        String template = """
            Eres un narrador de aventuras.
            Crea el inicio de una historia del género {genre}, con protagonista llamado {name}, 
            ubicado en {location}. 
            El protagonista tiene esta descripción: {description}.
            
            La duración será {duration} (5, 10 o 20 decisiones en total).
            La complejidad de cada turno será {complexity} (2, 3 o 5 opciones).
            Debes devolver SOLO el primer fragmento de la historia y las opciones de decisión iniciales.
            
            Recuerda incluir al automóvil Megalodón y al menos uno de sus features en la narrativa.
            """;

        // Seteo de variables al prompt
        PromptTemplate prompt = new PromptTemplate(template);
        prompt.add("genre", req.getGenre());
        prompt.add("name", req.getMainCharacterName());
        prompt.add("description", req.getMainCharacterDescription());
        prompt.add("duration", req.getDuration());
        prompt.add("complexity", req.getComplexity());
        prompt.add("location", req.getLocation());

        return chatClient.prompt(prompt.create()).call().content();
    }
}
