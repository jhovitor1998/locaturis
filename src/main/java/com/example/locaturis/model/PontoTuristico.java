package com.example.locaturis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pontos_turisticos")
public class PontoTuristico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String cidade;
    private String estado;

    // --- NOVOS CAMPOS PARA "COMO CHEGAR" ---
    private Double latitude;
    private Double longitude;
    private String comoChegarTexto;

    // Campos de avaliação
    private Double notaMedia = 0.0;
    private Integer totalAvaliacoes = 0;

    // Relacionamento com Hospedagem
    @OneToMany(mappedBy = "ponto", cascade = CascadeType.ALL)
    private List<Hospedagem> hospedagens;

    private LocalDateTime createdAt = LocalDateTime.now();
}