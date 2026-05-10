package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
import com.sanosysalvos.bff.service.PublicacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms-publicacion")
@Tag(name = "Publicaciones", description = "CRUD para gestionar publicaciones de mascotas")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping(value = "/publicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar publicaciones", description = "Obtiene todas las publicaciones registradas desde el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array"),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "idPublicacion": "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a",
                                        "tipoPublicacion": "PERDIDA",
                                        "titulo": "Se busca perro mestizo en Providencia"
                                      }
                                    ]
                                    """)))
    })
    public ResponseEntity<String> getAll() {
        return publicacionService.getAll();
    }

    @GetMapping(value = "/publicaciones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener publicacion por ID", description = "Obtiene una publicacion especifica desde el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Publicacion encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "404", description = "Publicacion no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object")))
    })
    public ResponseEntity<String> getById(@PathVariable String id) {
        return publicacionService.getById(id);
    }

    @PostMapping(value = "/publicaciones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear publicacion", description = "Crea una nueva publicacion en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Publicacion creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Datos de la publicacion",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = """
                            {
                              "tipoPublicacion": "PERDIDA",
                              "titulo": "Se busca perro mestizo en Providencia",
                              "descripcion": "Perro mediano color cafe con collar azul",
                              "fechaPublicacion": "2026-03-25T15:30:00",
                              "fechaExtravioOEncuentro": "2026-03-24",
                              "estado": "ACTIVA",
                              "latitud": -33.4489,
                              "longitud": -70.6693,
                              "direccionReferencia": "Cerca de Plaza Italia",
                              "urlFoto": "https://example.com/foto.jpg",
                              "nombreContacto": "Maria Perez",
                              "telefonoContacto": "+56912345678",
                              "emailContacto": "maria@example.com",
                              "mascotaId": "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a",
                              "usuarioId": "2df6d4ba-ef5e-4ad3-a148-ecb48ff8f933"
                            }
                            """)))
    public ResponseEntity<String> create(@RequestBody Map<String, Object> request) {
        return publicacionService.create(request);
    }

    @PutMapping(value = "/publicaciones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar publicacion", description = "Actualiza una publicacion existente en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Publicacion actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "404", description = "Publicacion no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Datos actualizados de la publicacion",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = """
                            {
                              "tipoPublicacion": "ENCONTRADA",
                              "titulo": "Perro encontrado en Providencia",
                              "descripcion": "Perro mediano color cafe sin collar",
                              "fechaPublicacion": "2026-03-25T15:30:00",
                              "fechaExtravioOEncuentro": "2026-03-24",
                              "estado": "ACTIVA",
                              "latitud": -33.4489,
                              "longitud": -70.6693,
                              "direccionReferencia": "Cerca de Plaza Italia",
                              "urlFoto": "https://example.com/foto.jpg",
                              "nombreContacto": "Maria Perez",
                              "telefonoContacto": "+56912345678",
                              "emailContacto": "maria@example.com",
                              "mascotaId": "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a",
                              "usuarioId": "2df6d4ba-ef5e-4ad3-a148-ecb48ff8f933"
                            }
                            """)))
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody Map<String, Object> request) {
        return publicacionService.update(id, request);
    }

    @DeleteMapping(value = "/publicaciones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Eliminar publicacion", description = "Elimina una publicacion por ID en el microservicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Publicacion eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Publicacion no encontrada")
    })
    public ResponseEntity<String> delete(@PathVariable String id) {
        return publicacionService.delete(id);
    }


}