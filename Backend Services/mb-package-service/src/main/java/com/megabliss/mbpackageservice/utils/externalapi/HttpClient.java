package com.megabliss.mbpackageservice.utils.externalapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {
    @Autowired
    private RestTemplate restTemplate;

    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> get(@NonNull String url, @NonNull ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> post(@NonNull String url, Object request,
            @NonNull ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> put(@NonNull String url, Object request, @NonNull Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
    }

    public <T> ResponseEntity<T> delete(@NonNull String url, @NonNull Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
    }
}
