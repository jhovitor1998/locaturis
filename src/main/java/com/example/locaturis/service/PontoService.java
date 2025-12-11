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
    private final FavoritoRepository favoritoRepo;

    @Value("${app.upload.dir}")
    private String pastaUpload;

    public PontoService(PontoRepository pontoRepo, FotoRepository fotoRepo, 
                        ComentarioRepository comentarioRepo, UsuarioRepository usuarioRepo,
                        HospedagemRepository hospedagemRepo, FavoritoRepository favoritoRepo) {
        this.pontoRepo = pontoRepo;
        this.fotoRepo = fotoRepo;
        this.comentarioRepo = comentarioRepo;
        this.usuarioRepo = usuarioRepo;
        this.hospedagemRepo = hospedagemRepo;
        this.favoritoRepo = favoritoRepo;
    }

    // --- USUÁRIO ---
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail())) throw new IllegalArgumentException("Email já cadastrado.");
        if (usuario.getRole() == null) usuario.setRole(Usuario.Role.USER);
        return usuarioRepo.save(usuario);
    }

    public Usuario login(LoginDTO login) {
        Optional<Usuario> userOpt = usuarioRepo.findByEmail(login.getEmail());
        if (userOpt.isPresent() && userOpt.get().getSenha().equals(login.getSenha())) {
            return userOpt.get();
        }
        throw new IllegalArgumentException("Credenciais inválidas.");
    }

    // --- PONTO ---
    public List<PontoTuristico> listarPontos(String cidade) {
        if (cidade != null && !cidade.isEmpty()) {
            return pontoRepo.findAll().stream()
                    .filter(p -> p.getCidade().equalsIgnoreCase(cidade)).toList();
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
        PontoTuristico ponto = pontoRepo.findById(id).orElseThrow();
        return Map.of("latitude", ponto.getLatitude() != null ? ponto.getLatitude() : 0.0,
                      "longitude", ponto.getLongitude() != null ? ponto.getLongitude() : 0.0,
                      "instrucoes", ponto.getComoChegarTexto() != null ? ponto.getComoChegarTexto() : "",
                      "googleMapsLink", "https://www.google.com/maps/search/?api=1&query=" + ponto.getLatitude() + "," + ponto.getLongitude());
    }

    // --- FOTOS ---
    public Foto salvarFoto(Long pontoId, MultipartFile arquivo) throws IOException {
        if (fotoRepo.countByPontoId(pontoId) >= 10) {
            throw new IllegalArgumentException("Limite de 10 fotos atingido.");
        }

        Path diretorio = Paths.get(pastaUpload);
        if (!Files.exists(diretorio)) Files.createDirectories(diretorio);
        String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
        Path caminhoFinal = diretorio.resolve(nomeArquivo);
        Files.copy(arquivo.getInputStream(), caminhoFinal, StandardCopyOption.REPLACE_EXISTING);

        Foto foto = new Foto();
        foto.setPontoId(pontoId);
        foto.setNomeArquivo(nomeArquivo);
        foto.setCaminhoCompleto(caminhoFinal.toString());
        
        // --- ATUALIZAÇÃO: Define como capa do Ponto no PostgreSQL ---
        PontoTuristico ponto = pontoRepo.findById(pontoId).orElseThrow();
        ponto.setImagemCapa("/uploads/" + nomeArquivo); // Salva o link direto
        pontoRepo.save(ponto);
        // -----------------------------------------------------------

        return fotoRepo.save(foto);
    }
    public List<Foto> listarFotos(Long pontoId) { return fotoRepo.findByPontoId(pontoId); }

    // --- COMENTÁRIOS ---
    public Comentario adicionarComentario(Long pontoId, Comentario comentario) {
        if (comentario.getNota() < 1 || comentario.getNota() > 5) throw new IllegalArgumentException("Nota 1-5.");
        
        Optional<Comentario> existente = comentarioRepo.findByPontoIdAndEmailUsuario(pontoId, comentario.getEmailUsuario());
        if (existente.isPresent()) {
            Comentario antigo = existente.get();
            antigo.setTexto(comentario.getTexto());
            antigo.setNota(comentario.getNota());
            antigo.setData(java.time.LocalDateTime.now());
            comentarioRepo.save(antigo);
        } else {
            comentario.setPontoId(pontoId);
            comentarioRepo.save(comentario);
        }
        atualizarMediaPonto(pontoId);
        return comentario;
    }

    public void excluirComentario(String comentarioId, Long usuarioId) {
        Comentario c = comentarioRepo.findById(comentarioId).orElseThrow(() -> new RuntimeException("Comentário não existe"));
        Usuario u = usuarioRepo.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não existe"));
        
        boolean isDono = u.getEmail().equals(c.getEmailUsuario());
        boolean isAdmin = u.getRole() == Usuario.Role.ADMIN;

        if (isDono || isAdmin) {
            comentarioRepo.delete(c);
            atualizarMediaPonto(c.getPontoId());
        } else {
            throw new RuntimeException("Sem permissão para excluir.");
        }
    }

    private void atualizarMediaPonto(Long pontoId) {
        List<Comentario> todos = comentarioRepo.findByPontoIdOrderByDataDesc(pontoId);
        if (todos.isEmpty()) return;
        double media = todos.stream().mapToInt(Comentario::getNota).average().orElse(0.0);
        PontoTuristico ponto = pontoRepo.findById(pontoId).orElseThrow();
        ponto.setNotaMedia(media);
        ponto.setTotalAvaliacoes(todos.size());
        pontoRepo.save(ponto);
    }

    public List<Comentario> listarComentarios(Long pontoId) { return comentarioRepo.findByPontoIdOrderByDataDesc(pontoId); }

    // --- FAVORITOS ---
    public boolean toggleFavorito(Long usuarioId, Long pontoId) {
        Optional<Favorito> fav = favoritoRepo.findByUsuarioIdAndPontoId(usuarioId, pontoId);
        if (fav.isPresent()) {
            favoritoRepo.delete(fav.get());
            return false;
        } else {
            Favorito novo = new Favorito();
            novo.setUsuario(usuarioRepo.findById(usuarioId).get());
            novo.setPonto(pontoRepo.findById(pontoId).get());
            favoritoRepo.save(novo);
            return true;
        }
    }
    
    // --- HOSPEDAGEM ---
    public Hospedagem adicionarHospedagem(Long pontoId, Hospedagem h) {
        PontoTuristico p = pontoRepo.findById(pontoId).orElseThrow();
        h.setPonto(p);
        return hospedagemRepo.save(h);
    }
    public List<Hospedagem> listarHospedagens(Long pontoId) { return hospedagemRepo.findByPontoId(pontoId); }
}