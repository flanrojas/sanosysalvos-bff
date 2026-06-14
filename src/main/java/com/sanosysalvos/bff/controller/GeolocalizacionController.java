package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.client.GeolocalizacionClient;
import com.sanosysalvos.bff.dto.request.DistanceRequest;
import com.sanosysalvos.bff.dto.request.RadiusRequest;
import com.sanosysalvos.bff.dto.request.LocationCreateRequest;
import com.sanosysalvos.bff.dto.response.DistanceResponse;
import com.sanosysalvos.bff.dto.response.RadiusResponse;
import com.sanosysalvos.bff.dto.response.LocationResponse;
import com.sanosysalvos.bff.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/bff/geolocalizacion")
@Tag(name = "Geolocalización Políglota", description = "Endpoints que exponen la lógica GIS del microservicio en Python")
public class GeolocalizacionController {

    private final GeolocalizacionClient geolocalizacionClient;

    public GeolocalizacionController(GeolocalizacionClient geolocalizacionClient) {
        this.geolocalizacionClient = geolocalizacionClient;
    }

    @PostMapping("/distance")
    @Operation(summary = "Calcular distancia lineal entre dos coordenadas")
    public ResponseEntity<DistanceResponse> calcularDistancia(@RequestBody DistanceRequest request) {
        return ResponseEntity.ok(geolocalizacionClient.calcularDistancia(request));
    }

    @PostMapping("/radius")
    @Operation(summary = "Buscar IDs de mascotas perdidas dentro de un radio en metros")
    public ResponseEntity<RadiusResponse> buscarMascotasEnRadio(@RequestBody RadiusRequest request) {
        return ResponseEntity.ok(geolocalizacionClient.buscarMascotasEnRadio(request));
    }

    @PostMapping("/locations")
    @Operation(summary = "Guardar o actualizar (upsert) la última ubicación geoespacial de una mascota")
    public ResponseEntity<MessageResponse> guardarUbicacion(@RequestBody LocationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(geolocalizacionClient.guardarUbicacion(request));
    }

    @GetMapping("/locations/{petId}")
    @Operation(summary = "Obtener las coordenadas X/Y de una mascota específica")
    public ResponseEntity<LocationResponse> obtenerUbicacion(@PathVariable UUID petId) {
        return ResponseEntity.ok(geolocalizacionClient.obtenerUbicacion(petId));
    }

    @DeleteMapping("/locations/{petId}")
    @Operation(summary = "Remover el registro geoespacial de una mascota")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable UUID petId) {
        geolocalizacionClient.eliminarUbicacion(petId);
        return ResponseEntity.noContent().build();
    }
}