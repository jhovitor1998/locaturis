package com.example.locaturis.repository;
import com.example.locaturis.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    Optional<Favorito> findByUsuarioIdAndPontoId(Long usuarioId, Long pontoId);
}