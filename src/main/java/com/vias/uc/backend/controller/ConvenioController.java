package com.vias.uc.backend.controller;

import com.vias.uc.backend.model.dto.ConvenioInput;
import com.vias.uc.backend.model.dto.ConvenioOutput;
import com.vias.uc.backend.model.dto.ConvenioUpdateInput;
import com.vias.uc.backend.service.ConvenioService;
import com.vias.uc.backend.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de convenios
 * Implementa los casos de uso:
 * - UC-EMP-013: Solicitar Convenio
 * - UC-EMP-016: Administrar Convenios Vigentes
 * - UC-EMP-017: Ver Estado de Solicitudes
 */
@RestController
@RequestMapping("/api/v1/convenios")
@CrossOrigin(origins = "*")
public class ConvenioController {

    private final ConvenioService convenioService;
    private final AuthorizationService authorizationService;

    public ConvenioController(ConvenioService convenioService, 
                             AuthorizationService authorizationService) {
        this.convenioService = convenioService;
        this.authorizationService = authorizationService;
    }

    /**
     * UC-EMP-013: Solicitar Convenio
     * POST /api/v1/convenios/solicitar
     * TEMPORAL: Sin validación de token para testing
     */
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarConvenio(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) Long testUserId,
            @RequestBody ConvenioInput input) {
        
        try {
            // TEMPORAL: Para testing, usar header X-User-Id
            Long usuarioId = testUserId != null ? testUserId : 1002L; // 1002 = alfre_costa@hotmail.com
            
            // Comentado temporalmente para testing
            // Long usuarioId = authorizationService.getUserIdFromToken(authHeader);
            // if (usuarioId == null) {
            //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            //         .body(createErrorResponse("UNAUTHORIZED", "Token inválido o expirado"));
            // }
            // if (!authorizationService.isEmpresa(usuarioId)) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN)
            //         .body(createErrorResponse("FORBIDDEN", "Solo empresas pueden solicitar convenios"));
            // }

            ConvenioOutput convenio = convenioService.solicitarConvenio(input, usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Solicitud de convenio creada exitosamente");
            response.put("idConvenio", convenio.getIdConven());
            response.put("estado", convenio.getEstado());
            response.put("convenio", convenio);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * UC-EMP-017: Ver Estado de las Solicitudes
     * GET /api/v1/convenios/solicitudes
     * TEMPORAL: Sin validación de token para testing
     */
    @GetMapping("/solicitudes")
    public ResponseEntity<?> listarSolicitudes(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) Long testUserId) {
        
        try {
            // TEMPORAL: Para testing
            Long usuarioId = testUserId != null ? testUserId : 1002L;

            List<ConvenioOutput> solicitudes = convenioService.listarSolicitudes(usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("solicitudes", solicitudes);
            response.put("total", solicitudes.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * UC-EMP-016: Ver Convenios Vigentes
     * GET /api/v1/convenios/vigentes
     */
    @GetMapping("/vigentes")
    public ResponseEntity<?> listarConveniosVigentes(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            Long usuarioId = authorizationService.getUserIdFromToken(authHeader);

            List<ConvenioOutput> convenios = convenioService.listarConveniosVigentes(usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("convenios", convenios);
            response.put("total", convenios.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * Obtener detalle de un convenio
     * GET /api/v1/convenios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleConvenio(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer id) {
        
        try {
            Long usuarioId = authorizationService.getUserIdFromToken(authHeader);
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("UNAUTHORIZED", "Token inválido o expirado"));
            }

            if (!authorizationService.isEmpresa(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("FORBIDDEN", "Solo empresas pueden ver convenios"));
            }

            Optional<ConvenioOutput> convenio = convenioService.obtenerDetalleConvenio(id, usuarioId);
            
            if (convenio.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("NOT_FOUND", "Convenio no encontrado"));
            }
            
            return ResponseEntity.ok(convenio.get());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * UC-EMP-016: Actualizar Responsable de Convenio
     * PUT /api/v1/convenios/{id}/responsable
     */
    @PutMapping("/{id}/responsable")
    public ResponseEntity<?> actualizarResponsable(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer id,
            @RequestBody ConvenioUpdateInput input) {
        
        try {
            Long usuarioId = authorizationService.getUserIdFromToken(authHeader);
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("UNAUTHORIZED", "Token inválido o expirado"));
            }

            if (!authorizationService.isEmpresa(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("FORBIDDEN", "Solo empresas pueden actualizar convenios"));
            }

            ConvenioOutput convenio = convenioService.actualizarResponsable(id, input, usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Responsable actualizado exitosamente");
            response.put("convenio", convenio);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * UC-EMP-016: Solicitar Renovación de Convenio
     * POST /api/v1/convenios/{id}/renovar
     */
    @PostMapping("/{id}/renovar")
    public ResponseEntity<?> solicitarRenovacion(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer id) {
        
        try {
            Long usuarioId = authorizationService.getUserIdFromToken(authHeader);
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("UNAUTHORIZED", "Token inválido o expirado"));
            }

            if (!authorizationService.isEmpresa(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("FORBIDDEN", "Solo empresas pueden renovar convenios"));
            }

            ConvenioOutput convenio = convenioService.solicitarRenovacion(id, usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Solicitud de renovación enviada exitosamente");
            response.put("convenio", convenio);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * UC-EMP-016: Dar de Baja Convenio
     * POST /api/v1/convenios/{id}/baja
     */
    @PostMapping("/{id}/baja")
    public ResponseEntity<?> darDeBajaConvenio(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, String> body) {
        
        try {
            Long usuarioId = authorizationService.getUserIdFromToken(authHeader);
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("UNAUTHORIZED", "Token inválido o expirado"));
            }

            if (!authorizationService.isEmpresa(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("FORBIDDEN", "Solo empresas pueden dar de baja convenios"));
            }

            String motivo = (body != null && body.containsKey("motivo")) 
                ? body.get("motivo") 
                : "Convenio dado de baja por la empresa";
            
            ConvenioOutput convenio = convenioService.darDeBajaConvenio(id, motivo, usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Convenio dado de baja exitosamente");
            response.put("convenio", convenio);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * Método auxiliar para crear respuestas de error
     */
    private Map<String, String> createErrorResponse(String code, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return error;
    }
}
