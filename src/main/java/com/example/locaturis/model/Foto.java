package com.example.locaturis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "fotos") 
public class Foto {
    @Id
    private String id;
    
    private Long pontoId; // Conecta com o ID do Postgres
    private String nomeArquivo;
    private String caminhoCompleto; 
    private LocalDateTime dataUpload = LocalDateTime.now();
}