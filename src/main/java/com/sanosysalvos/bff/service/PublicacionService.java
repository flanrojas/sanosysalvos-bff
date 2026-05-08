package com.sanosysalvos.bff.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class PublicacionService {

    private static final String PUBLICACIONES_PATH = "/publicaciones";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PublicacionService(RestTemplate restTemplate,
                              @Value("${ms.publicacion.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<String> getAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders)
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if (ex.getResponseHeaders() != null && ex.getResponseHeaders().getContentType() != null) {
                responseHeaders.setContentType(ex.getResponseHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders)
                    .body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> getById(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH + "/" + id,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders)
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if (ex.getResponseHeaders() != null && ex.getResponseHeaders().getContentType() != null) {
                responseHeaders.setContentType(ex.getResponseHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders)
                    .body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> create(Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders)
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if (ex.getResponseHeaders() != null && ex.getResponseHeaders().getContentType() != null) {
                responseHeaders.setContentType(ex.getResponseHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders)
                    .body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> update(String id, Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH + "/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders)
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if (ex.getResponseHeaders() != null && ex.getResponseHeaders().getContentType() != null) {
                responseHeaders.setContentType(ex.getResponseHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders)
                    .body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> delete(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH + "/" + id,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders)
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if (ex.getResponseHeaders() != null && ex.getResponseHeaders().getContentType() != null) {
                responseHeaders.setContentType(ex.getResponseHeaders().getContentType());
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders)
                    .body(ex.getResponseBodyAsString());
        }
    }
}
