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
@RequestMapping("/user-ticket")
@RequiredArgsConstructor
public class UserTicketController {
    private final RestTemplate restTemplate;

    @Value("${service.base.url}")
    private String baseUrl;

    @GetMapping()
    public ResponseEntity<String> getUserTickets() {
        String url = baseUrl + "/users-tickets";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserTicketById(@PathVariable Long id) {
        String url = baseUrl + "/users-tickets/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<String> createUserTicket(@RequestBody JsonNode userTicket) {
        String url = baseUrl + "/users-tickets";
        String response = restTemplate.postForObject(url, userTicket, String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserTicket(@PathVariable Long id, @RequestBody JsonNode userTicket) {
        String url = baseUrl + "/users-tickets/" + id;
        restTemplate.put(url, userTicket);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/qrcode/{qrcode}")
    public ResponseEntity<String> updateUserTicketByQrCode(@PathVariable String qrcode, @RequestBody JsonNode userTicket) {
        String url = baseUrl + "/users-tickets/qrcode/" + qrcode;
        restTemplate.put(url, userTicket);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTicket(@PathVariable Long id) {
        String url = baseUrl + "/users-tickets/" + id;
        restTemplate.delete(url);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
