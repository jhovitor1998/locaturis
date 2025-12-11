package com.example.locaturis.repository;
import com.example.locaturis.model.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
    List<Hospedagem> findByPontoId(Long pontoId);
}