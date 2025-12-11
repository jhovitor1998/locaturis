package com.example.locaturis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "comentarios")
public class Comentario {
    @Id
    private String id;
    private Long pontoId;
    
    private String nomeUsuario;
    private String emailUsuario; // Importante para validar unicidade/permissão
    
    private String texto;
    private Integer nota;
    private LocalDateTime data = LocalDateTime.now();
}