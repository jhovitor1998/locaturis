package com.example.locaturis.repository;
import com.example.locaturis.model.Foto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FotoRepository extends MongoRepository<Foto, String> {
    List<Foto> findByPontoId(Long pontoId);
    long countByPontoId(Long pontoId);
}