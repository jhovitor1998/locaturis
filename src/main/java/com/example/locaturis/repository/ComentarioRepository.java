package com.example.locaturis.repository;

import com.example.locaturis.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Atenção: Estende MongoRepository (NoSQL), e não JpaRepository
public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    
    // O Spring cria essa busca automaticamente baseado no nome do campo "pontoId"
    // que criamos na classe Comentario
    List<Comentario> findByPontoId(Long pontoId);
}