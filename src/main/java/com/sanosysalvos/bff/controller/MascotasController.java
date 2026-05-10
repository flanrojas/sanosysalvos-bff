package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.service.MascotasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms-mascotas")
@Tag(name = "Mascotas", description = "CRUD para gestionar mascotas desde el BFF")
public class MascotasController {

    private static final Set<String> VALID_STATUSES = Set.of("LOST", "SEARCHING", "FOUND", "AT_HOME");

    private final MascotasService mascotasService;

    public MascotasController(MascotasService mascotasService) {
        this.mascotasService = mascotasService;
    }

    @GetMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar mascotas", description = "Obtiene mascotas desde el microservicio, opcionalmente filtradas por dueno o estado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))),
            @ApiResponse(responseCode = "204", description = "Sin mascotas para los filtros enviados"),
            @ApiResponse(responseCode = "400", description = "Filtros invalidos")
    })
    public ResponseEntity<String> getAll(
            @Parameter(description = "ID UUID del dueno") @RequestParam(required = false) String ownerId,
            @Parameter(description = "Estado: LOST, SEARCHING, FOUND, AT_HOME") @RequestParam(required = false) String status) {
        if (ownerId != null && !isUuid(ownerId)) {
            return badRequest("ownerId debe ser un UUID valido");
        }
        if (status != null && !isValidStatus(status)) {
            return badRequest("status debe ser uno de: LOST, SEARCHING, FOUND, AT_HOME");
        }
        return mascotasService.getAll(ownerId, status);
    }

    @GetMapping(value = "/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener mascota por ID", description = "Obtiene el detalle de una mascota desde el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<String> getById(@PathVariable String id) {
        if (!isUuid(id)) {
            return badRequest("id debe ser un UUID valido");
        }
        return mascotasService.getById(id);
    }

    @PostMapping(value = "/pets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear mascota", description = "Crea una mascota en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mascota creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Datos de la mascota",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Firulais",
                              "status": "LOST",
                              "species": "Perro",
                              "color": "Cafe con manchas blancas",
                              "size": 15.5,
                              "foundLocation": null,
                              "lostLocation": "Calle Arturo Prat 123",
                              "description": "Tiene un collar azul sin placa",
                              "ownerId": "550e8400-e29b-41d4-a716-446655440000"
                            }
                            """)))
    public ResponseEntity<String> create(@RequestBody Map<String, Object> request) {
        ResponseEntity<String> validation = validateRequest(request, false);
        if (validation != null) {
            return validation;
        }
        return mascotasService.create(request);
    }

    @PutMapping(value = "/pets/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar mascota", description = "Reemplaza o actualiza una mascota existente en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody Map<String, Object> request) {
        if (!isUuid(id)) {
            return badRequest("id debe ser un UUID valido");
        }
        ResponseEntity<String> validation = validateRequest(request, false);
        if (validation != null) {
            return validation;
        }
        return mascotasService.update(id, request);
    }

    @PatchMapping(value = "/pets/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente mascota", description = "Actualiza solo los campos enviados de una mascota")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<String> patch(@PathVariable String id, @RequestBody Map<String, Object> request) {
        if (!isUuid(id)) {
            return badRequest("id debe ser un UUID valido");
        }
        ResponseEntity<String> validation = validateRequest(request, true);
        if (validation != null) {
            return validation;
        }
        return mascotasService.patch(id, request);
    }

    @DeleteMapping(value = "/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Eliminar mascota", description = "Elimina una mascota por ID en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mascota eliminada"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (!isUuid(id)) {
            return badRequest("id debe ser un UUID valido");
        }
        return mascotasService.delete(id);
    }

    private ResponseEntity<String> validateRequest(Map<String, Object> request, boolean partial) {
        if (request == null || request.isEmpty()) {
            return badRequest("body no puede estar vacio");
        }
        if ((!partial || request.containsKey("status")) && !isValidStatusValue(request.get("status"))) {
            return badRequest("status debe ser uno de: LOST, SEARCHING, FOUND, AT_HOME");
        }
        if (request.containsKey("ownerId") && request.get("ownerId") != null && !isUuid(String.valueOf(request.get("ownerId")))) {
            return badRequest("ownerId debe ser un UUID valido");
        }
        return null;
    }

    private boolean isValidStatusValue(Object status) {
        return status instanceof String value && isValidStatus(value);
    }

    private boolean isValidStatus(String status) {
        return VALID_STATUSES.contains(status);
    }

    private boolean isUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\":\"" + message + "\"}");
    }
}
