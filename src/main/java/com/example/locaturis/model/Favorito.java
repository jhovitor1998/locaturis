package com.example.locaturis.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "favoritos")
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ponto_id")
    private PontoTuristico ponto;
}