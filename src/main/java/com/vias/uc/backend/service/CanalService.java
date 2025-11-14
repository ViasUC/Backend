package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Publicacion;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.canales.CanalInformacion;
import com.vias.uc.backend.model.canales.CanalPublicacion;
import com.vias.uc.backend.model.canales.CanalPublicacionId;
import com.vias.uc.backend.model.enums.EstadoPublicacion;
import com.vias.uc.backend.repository.PublicacionRepository;
import com.vias.uc.backend.repository.canales.CanalInformacionRepository;
import com.vias.uc.backend.repository.canales.CanalPublicacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.vias.uc.backend.model.canales.CanalSeguidor;
import com.vias.uc.backend.model.canales.CanalSeguidorId;
import com.vias.uc.backend.repository.canales.CanalSeguidorRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.model.enums.TipoCanal;
import com.vias.uc.backend.model.enums.RolUsuario;



import java.time.Instant;
import java.time.LocalDateTime;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class CanalService {

    private final CanalInformacionRepository canalRepo;
    private final CanalPublicacionRepository canalPubRepo;
    private final PublicacionRepository publicacionRepo;
    private final AuditoriaService auditoria;

    private final CanalSeguidorRepository canalSeguidorRepo;   // NUEVO
    private final UsuarioRepository usuarioRepo;

    public CanalService(CanalInformacionRepository canalRepo,
                        CanalPublicacionRepository canalPubRepo,
                        PublicacionRepository publicacionRepo,
                        CanalSeguidorRepository canalSeguidorRepo,
                        UsuarioRepository usuarioRepo,           // <<--- NUEVO
                        AuditoriaService auditoria) {
        this.canalRepo = canalRepo;
        this.canalPubRepo = canalPubRepo;
        this.publicacionRepo = publicacionRepo;
        this.canalSeguidorRepo = canalSeguidorRepo;
        this.usuarioRepo = usuarioRepo;                        // <<--- NUEVO
        this.auditoria = auditoria;
    }

    // 1) Listar canales activos
    public List<CanalInformacion> canalesActivos() {
        return canalRepo.findAllByActivoTrue();
    }

    // 2) Crear canal (la universidad/administrador)
    public CanalInformacion crearCanal(String nombre,
                                       String slug,
                                       TipoCanal tipo,
                                       String descripcion,
                                       Long actorId) {

        // 0) Buscar al actor que intenta crear el canal
        Usuario actor = usuarioRepo.findById(actorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario (actor) no encontrado"));

        // 0.1) Validar que sea ADMINISTRADOR, PROFESOR o INVESTIGADOR
        RolUsuario rol = actor.getRolPrincipal();
        if (rol != RolUsuario.administrador &&
                rol != RolUsuario.profesor &&
                rol != RolUsuario.investigador) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo administrador, profesor o investigador pueden crear canales."
            );
        }

        // 1) Registrar auditoría (puede seguir usando actorId sin problema)
        Long idAud = Long.valueOf(
                auditoria.log(Math.toIntExact(actorId), "canales.create", "slug=" + slug)
        );

        // 2) Crear el canal
        CanalInformacion c = new CanalInformacion();
        c.setNombre(nombre);
        c.setSlug(slug);
        c.setTipo(tipo);          // enum
        c.setDescripcion(descripcion);
        c.setActivo(true);
        c.setIdAuditoria(Math.toIntExact(idAud));

        return canalRepo.save(c);
    }



    // 3) Listar publicaciones de un canal
    public List<Publicacion> publicacionesDeCanal(Integer idCanal) {
        return canalPubRepo
                .findAllByCanal_IdCanalOrderByPublicacion_FechaPublicacionDesc(idCanal)
                .stream()
                .map(CanalPublicacion::getPublicacion)
                .toList();
    }


    // 4) Crear publicación NUEVA de un docente/investigador y vincularla a un canal
    public Publicacion crearPublicacionEnCanal(Integer idCanal, Integer idProyectoF7, Integer idProfesor, String contenido) {
        // 1) Verificar que el canal exista
        CanalInformacion canal = canalRepo.findById(idCanal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal no encontrado"));

        // 2) Traer el usuario autor (idAutor ahora es id_usuario) y validar rol
        Usuario autor = usuarioRepo.findById(Long.valueOf(idProfesor))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario (autor) no encontrado"));

        RolUsuario rol = autor.getRolPrincipal();
        if (rol != RolUsuario.profesor &&
                rol != RolUsuario.investigador &&
                rol != RolUsuario.administrador &&
                rol != RolUsuario.egresado &&
                rol != RolUsuario.empresa) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo usuarios autorizados pueden publicar en canales"
            );
        }

        // 3) Crear auditoría
        int idAud = Math.toIntExact(Long.valueOf(
                auditoria.log(idProfesor, "canales.publicar", "canal=" + idCanal)
        ));

        // 4) Crear publicación
        Publicacion pub = new Publicacion();
        pub.setIdProyecto(idProyectoF7);      // proyecto genérico F7
        pub.setIdReporte(null);               // para cumplir el CHECK
        pub.setAutor(autor);                  // <<< ACA se llena id_autor
        pub.setEstado(EstadoPublicacion.publicado);
        pub.setFechaPublicacion(LocalDateTime.now());
        pub.setObservacion(contenido);
        pub.setIdAuditoria(idAud);

        pub = publicacionRepo.save(pub);

        // 5) Vincular a canal
        CanalPublicacionId cpId = new CanalPublicacionId(idCanal, pub.getIdPublicacion());

        CanalPublicacion cp = new CanalPublicacion();
        cp.setId(cpId);
        cp.setCanal(canal);
        cp.setPublicacion(pub);
        cp.setDestacado(false);

        canalPubRepo.save(cp);

        return pub;
    }



    public boolean seguirCanal(Integer idCanal, Integer idUsuario) {
        CanalInformacion canal = canalRepo.findById(idCanal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal no encontrado"));

        // 1) Traer el usuario (NO puede ser null por el @MapsId)
        Usuario usuario = usuarioRepo.findById(Long.valueOf(idUsuario))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        CanalSeguidorId csId = new CanalSeguidorId(idCanal, idUsuario);

        if (canalSeguidorRepo.existsById(csId)) {
            return false; // ya seguía el canal ok
        }

        Long idAud = Long.valueOf(auditoria.log(idUsuario, "canales.follow",
                "canal=" + idCanal));

        // 2) Usar el constructor que ya tenés, que setea id/canal/usuario/fechaAlta
        CanalSeguidor cs = new CanalSeguidor(usuario, canal);
        cs.setIdAuditoria(idAud);

        canalSeguidorRepo.save(cs);
        return true;
    }


    @Transactional
    public boolean dejarDeSeguirCanal(Integer idCanal, Integer idUsuario) {

        // Validar que existan canal y usuario (coherente con seguir canal)
        CanalInformacion canal = canalRepo.findById(idCanal)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Canal no encontrado"));

        Usuario usuario = usuarioRepo.findById(Long.valueOf(idUsuario))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Buscar el seguidor
        return canalSeguidorRepo.findByCanal_IdCanalAndUsuario_IdUsuario(idCanal, idUsuario)
                .map(seguidor -> {
                    canalSeguidorRepo.delete(seguidor);

                    return true;
                })
                .orElse(false); // no estaba siguiendo, devolvés false (o podés tirar error si preferís)
    }


    public List<CanalInformacion> canalesSeguidosPorUsuario(Integer idUsuario) {
        return canalSeguidorRepo.findAllById_IdUsuario(idUsuario)
                .stream()
                .map(CanalSeguidor::getCanal)
                .toList();
    }

    public List<Publicacion> feedCanalesSeguidos(Integer idUsuario) {
        List<Integer> idsCanales = canalSeguidorRepo.findAllById_IdUsuario(idUsuario)
                .stream()
                .map(cs -> cs.getId().getIdCanal())
                .toList();

        if (idsCanales.isEmpty()) return List.of();

        return canalPubRepo
                .findAllByCanal_IdCanalInOrderByPublicacion_FechaPublicacionDesc(idsCanales)
                .stream()
                .map(CanalPublicacion::getPublicacion)
                .toList();
    }

    @Transactional
    public boolean destacarPublicacion(Integer idCanal, Integer idPublicacion, boolean destacado) {

        CanalPublicacionId id = new CanalPublicacionId(idCanal, idPublicacion);

        CanalPublicacion cp = canalPubRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Publicación no asociada a este canal"));

        cp.setDestacado(destacado);
        canalPubRepo.save(cp);

        return true;
    }

    public List<CanalInformacion> obtenerCanalesPorTipo(TipoCanal tipo) {
        return canalRepo.findByTipo(tipo);
    }



}
