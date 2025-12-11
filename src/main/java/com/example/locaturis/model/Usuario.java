package com.example.locaturis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String nome;
    
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, USER

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role { ADMIN, USER }
}