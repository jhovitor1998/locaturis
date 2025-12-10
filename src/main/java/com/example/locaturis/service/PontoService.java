package com.example.locaturis.service;

import com.example.locaturis.dto.LoginDTO;
import com.example.locaturis.model.*;
import com.example.locaturis.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PontoService {

    private final PontoRepository pontoRepo;
    private final FotoRepository fotoRepo;
    private final ComentarioRepository comentarioRepo;
    private final UsuarioRepository usuarioRepo;
    private final HospedagemRepository hospedagemRepo;

    @Value("${app.upload.dir}")
    private String pastaUpload;

    public PontoService(PontoRepository pontoRepo, FotoRepository fotoRepo, 
                        ComentarioRepository comentarioRepo, UsuarioRepository usuarioRepo,
                        HospedagemRepository hospedagemRepo) {
        this.pontoRepo = pontoRepo;
        this.fotoRepo = fotoRepo;
        this.comentarioRepo = comentarioRepo;
        this.usuarioRepo = usuarioRepo;
        this.hospedagemRepo = hospedagemRepo;
    }

    // --- USUÁRIO & LOGIN ---
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        // Define role padrão se vier nulo
        if (usuario.getRole() == null) usuario.setRole(Usuario.Role.USER);
        return usuarioRepo.save(usuario);
    }

    public Usuario login(LoginDTO login) {
        Optional<Usuario> userOpt = usuarioRepo.findByEmail(login.getEmail());
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            // Comparação simples de senha (para o MVP)
            if (user.getSenha().equals(login.getSenha())) {
                return user;
            }
        }
        throw new IllegalArgumentException("Email ou senha inválidos.");
    }

    // --- PONTO TURÍSTICO ---
    public List<PontoTuristico> listarPontos(String cidade) {
        // Filtro simples por cidade (Requisito PDF)
        if (cidade != null && !cidade.isEmpty()) {
            return pontoRepo.findAll().stream()
                    .filter(p -> p.getCidade().equalsIgnoreCase(cidade))
                    .toList();
        }
        return pontoRepo.findAll();
    }

    public PontoTuristico salvarPonto(PontoTuristico ponto) {
        if (ponto.getId() == null && pontoRepo.existsByNomeAndCidade(ponto.getNome(), ponto.getCidade())) {
            throw new IllegalArgumentException("Duplicidade detectada.");
        }
        return pontoRepo.save(ponto);
    }

    public Map<String, Object> comoChegar(Long id) {
        PontoTuristico ponto = pontoRepo.findById(id).orElseThrow(() -> new RuntimeException("Ponto não achado"));
        return Map.of(
            "latitude", ponto.getLatitude() != null ? ponto.getLatitude() : 0.0,
            "longitude", ponto.getLongitude() != null ? ponto.getLongitude() : 0.0,
            "instrucoes", ponto.getComoChegarTexto() != null ? ponto.getComoChegarTexto() : "Sem instruções.",
            "googleMapsLink", "https://www.google.com/maps/search/?api=1&query=" + ponto.getLatitude() + "," + ponto.getLongitude()
        );
    }

    // --- HOSPEDAGEM ---
    public Hospedagem adicionarHospedagem(Long pontoId, Hospedagem hospedagem) {
        PontoTuristico ponto = pontoRepo.findById(pontoId).orElseThrow(() -> new RuntimeException("Ponto não existe"));
        hospedagem.setPonto(ponto);
        return hospedagemRepo.save(hospedagem);
    }

    // --- FOTOS & COMENTÁRIOS (Mantidos iguais) ---
    public Foto salvarFoto(Long pontoId, MultipartFile arquivo) throws IOException {
        if (!pontoRepo.existsById(pontoId)) throw new RuntimeException("Ponto não encontrado.");
        Path diretorio = Paths.get(pastaUpload);
        if (!Files.exists(diretorio)) Files.createDirectories(diretorio);
        String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
        Path caminhoFinal = diretorio.resolve(nomeArquivo);
        Files.copy(arquivo.getInputStream(), caminhoFinal, StandardCopyOption.REPLACE_EXISTING);
        Foto foto = new Foto();
        foto.setPontoId(pontoId);
        foto.setNomeArquivo(nomeArquivo);
        foto.setCaminhoCompleto(caminhoFinal.toString());
        return fotoRepo.save(foto);
    }
    
    public List<Foto> listarFotos(Long pontoId) { return fotoRepo.findByPontoId(pontoId); }

    public Comentario adicionarComentario(Long pontoId, Comentario comentario) {
        if (comentario.getNota() < 1 || comentario.getNota() > 5) throw new IllegalArgumentException("Nota 1-5.");
        PontoTuristico ponto = pontoRepo.findById(pontoId).orElseThrow();
        comentario.setPontoId(pontoId);
        Comentario salvo = comentarioRepo.save(comentario);
        double novaMedia = ((ponto.getNotaMedia() * ponto.getTotalAvaliacoes()) + comentario.getNota()) / (ponto.getTotalAvaliacoes() + 1);
        ponto.setNotaMedia(novaMedia);
        ponto.setTotalAvaliacoes(ponto.getTotalAvaliacoes() + 1);
        pontoRepo.save(ponto);
        return salvo;
    }
    
    public List<Comentario> listarComentarios(Long pontoId) { return comentarioRepo.findByPontoId(pontoId); }
}