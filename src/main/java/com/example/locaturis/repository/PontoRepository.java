package com.example.locaturis.repository;

import com.example.locaturis.model.PontoTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Ao estender JpaRepository recebemos .save(), .findAll(), .deleteById()
@Repository
public interface PontoRepository extends JpaRepository<PontoTuristico, Long> {
    boolean existsByNomeAndCidade(String nome, String cidade);
}