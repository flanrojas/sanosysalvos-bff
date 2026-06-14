package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.GeolocalizacionClient;
import com.sanosysalvos.bff.dto.request.DistanceRequest;
import com.sanosysalvos.bff.dto.response.DistanceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeolocalizacionController.class)
@Import(ObjectMapper.class)
public class GeolocalizacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
}