package com.example.locaturis.controller;

import com.example.locaturis.model.*;
import com.example.locaturis.service.PontoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/pontos")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) { this.service = service; }

    // Listar com Filtro Opcional de Cidade (?cidade=Paris)
    @GetMapping
    public List<PontoTuristico> listar(@RequestParam(required = false) String cidade) {
        return service.listarPontos(cidade);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PontoTuristico ponto) {
        try { return ResponseEntity.ok(service.salvarPonto(ponto)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    // Endpoint "Como Chegar"
    @GetMapping("/{id}/como-chegar")
    public ResponseEntity<?> comoChegar(@PathVariable Long id) {
        try { return ResponseEntity.ok(service.comoChegar(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    // Hospedagens
    @PostMapping("/{id}/hospedagens")
    public ResponseEntity<?> addHospedagem(@PathVariable Long id, @RequestBody Hospedagem hospedagem) {
        try { return ResponseEntity.ok(service.adicionarHospedagem(id, hospedagem)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    // Fotos e Comentários (Mesmos de antes)
    @PostMapping("/{id}/fotos")
    public ResponseEntity<?> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try { return ResponseEntity.ok(service.salvarFoto(id, file)); }
        catch (Exception e) { return ResponseEntity.internalServerError().body(e.getMessage()); }
    }
    
    @GetMapping("/{id}/fotos")
    public List<Foto> verFotos(@PathVariable Long id) { return service.listarFotos(id); }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> avaliar(@PathVariable Long id, @RequestBody Comentario comentario) {
        try { return ResponseEntity.ok(service.adicionarComentario(id, comentario)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @GetMapping("/{id}/comentarios")
    public List<Comentario> verComentarios(@PathVariable Long id) { return service.listarComentarios(id); }
}