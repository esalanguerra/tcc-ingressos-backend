package br.com.ingressos.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final RestTemplate restTemplate;

    @Value("${service.base.url}")
    private String baseUrl;

    @GetMapping()
    public ResponseEntity<String> getUsers() {
        String url = baseUrl + "/users";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        String url = baseUrl + "/users/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody JsonNode user) {
        String url = baseUrl + "/users";
        String response = restTemplate.postForObject(url, user, String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody JsonNode user) {
        String url = baseUrl + "/users/" + id;
        restTemplate.put(url, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String url = baseUrl + "/users/" + id;
        restTemplate.delete(url);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
