package com.counter.test_counter.external_client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
@RequiredArgsConstructor
public class OptiicHttpClient {

    private final String apiUrl = "https://api.optiic.dev/process";
    @Value("${optiic.token}")
    private final String apiKey;
    private final RestTemplate restTemplate;

    public String getResult(File imageFile) { // TODO: 13.11.2024 change string to some dto if necessary
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(imageFile));
        builder.part("apiKey", apiKey);

        HttpEntity<?> requestEntity = new HttpEntity<>(builder.build(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        String responseString = response.getBody();
        return responseString;
    }
}
