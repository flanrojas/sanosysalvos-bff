package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.GeolocalizacionClient;
import com.sanosysalvos.bff.dto.request.DistanceRequest;
import com.sanosysalvos.bff.dto.request.LocationCreateRequest;
import com.sanosysalvos.bff.dto.request.RadiusRequest;
import com.sanosysalvos.bff.dto.response.DistanceResponse;
import com.sanosysalvos.bff.dto.response.LocationResponse;
import com.sanosysalvos.bff.dto.response.MessageResponse;
import com.sanosysalvos.bff.dto.response.RadiusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = GeolocalizacionController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = EnableWebSecurity.class)
)
public class GeolocalizacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private GeolocalizacionClient geolocalizacionClient;

    @Test
    void calcularDistancia_DebeRetornar200_Y_DistanciaCorrecta() throws Exception {
        DistanceRequest request = new DistanceRequest(-33.45, -70.66, -33.44, -70.65);
        DistanceResponse mockResponse = new DistanceResponse(1500.5);

        when(geolocalizacionClient.calcularDistancia(any(DistanceRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/bff/geolocalizacion/distance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance_meters").value(1500.5));
    }

    @Test
    void buscarMascotasEnRadio_DebeRetornar200() throws Exception {
        RadiusRequest request = new RadiusRequest(-33.45, -70.66, 5000.0);

        RadiusResponse response = new RadiusResponse(java.util.List.of());

        when(geolocalizacionClient.buscarMascotasEnRadio(any(RadiusRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bff/geolocalizacion/radius")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pets").isArray())
                .andExpect(jsonPath("$.pets.length()").value(0));
    }

    @Test
    void guardarUbicacion_DebeRetornar201() throws Exception {
        UUID petId = UUID.randomUUID();
        LocationCreateRequest request = new LocationCreateRequest(petId, -33.45, -70.66);
        MessageResponse response = new MessageResponse("Ubicación guardada correctamente");

        when(geolocalizacionClient.guardarUbicacion(any(LocationCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bff/geolocalizacion/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Ubicación guardada correctamente"));
    }

    @Test
    void obtenerUbicacion_DebeRetornar200() throws Exception {
        UUID petId = UUID.randomUUID();
        LocationResponse response = new LocationResponse(petId, -33.45, -70.66);

        when(geolocalizacionClient.obtenerUbicacion(eq(petId))).thenReturn(response);

        mockMvc.perform(get("/bff/geolocalizacion/locations/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pet_id").value(petId.toString()))
                .andExpect(jsonPath("$.latitude").value(-33.45))
                .andExpect(jsonPath("$.longitude").value(-70.66));
    }

    @Test
    void eliminarUbicacion_DebeRetornar204() throws Exception {
        UUID petId = UUID.randomUUID();

        doNothing().when(geolocalizacionClient).eliminarUbicacion(petId);

        mockMvc.perform(delete("/bff/geolocalizacion/locations/{petId}", petId))
                .andExpect(status().isNoContent());
    }
}