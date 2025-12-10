package com.example.locaturis.controller;

import com.example.locaturis.dto.LoginDTO;
import com.example.locaturis.model.Usuario;
import com.example.locaturis.service.PontoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PontoService service;

    public AuthController(PontoService service) { this.service = service; }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(service.registrarUsuario(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try {
            Usuario user = service.login(login);
            return ResponseEntity.ok("Login com sucesso! Bem-vindo " + user.getNome());
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}