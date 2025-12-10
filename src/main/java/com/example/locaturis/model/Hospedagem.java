package com.example.locaturis.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "hospedagens")
public class Hospedagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double precoMedio;
    private String tipo; // Hotel, Pousada, Hostel
    private String linkReserva;

    @ManyToOne
    @JoinColumn(name = "ponto_id")
    @JsonIgnore // Evita loop infinito no JSON
    private PontoTuristico ponto;
}