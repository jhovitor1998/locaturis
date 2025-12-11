package com.example.locaturis.repository;
import com.example.locaturis.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    List<Comentario> findByPontoIdOrderByDataDesc(Long pontoId);
    Optional<Comentario> findByPontoIdAndEmailUsuario(Long pontoId, String emailUsuario);
}