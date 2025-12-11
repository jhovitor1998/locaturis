package com.example.locaturis.repository;
import com.example.locaturis.model.PontoTuristico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontoRepository extends JpaRepository<PontoTuristico, Long> {
    boolean existsByNomeAndCidade(String nome, String cidade);
}