package br.com.ingressos.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final RestTemplate restTemplate;

    @Value("${service.base.url}")
    private String baseUrl;

    @GetMapping()
    public ResponseEntity<String> getCheckouts() {
        String url = baseUrl + "/checkouts";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<String> createCheckout(@RequestBody JsonNode checkout) {
        String url = baseUrl + "/checkouts";
        String response = restTemplate.postForObject(url, checkout, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCheckout(@PathVariable String id) {
        String url = baseUrl + "/checkouts/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCheckout(@PathVariable String id, @RequestBody JsonNode checkout) {
        String url = baseUrl + "/checkouts/" + id;
        restTemplate.put(url, checkout, String.class);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCheckout(@PathVariable String id) {
        String url = baseUrl + "/checkouts/" + id;
        restTemplate.delete(url);
        return ResponseEntity.ok().build();
    }
}
