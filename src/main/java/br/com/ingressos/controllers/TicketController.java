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
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final RestTemplate restTemplate;

    @Value("${service.base.url}")
    private String baseUrl;

    @GetMapping()
    public ResponseEntity<String> getTickets() {
        String url = baseUrl + "/tickets";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTicketById(@PathVariable Long id) {
        String url = baseUrl + "/tickets/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<String> getTicketByAdminId(@PathVariable String id) {
        String url = baseUrl + "/tickets/admin/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<String> createTicket(@RequestBody JsonNode ticket) {
        String url = baseUrl + "/tickets";
        String response = restTemplate.postForObject(url, ticket, String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTicket(@PathVariable Long id, @RequestBody JsonNode ticket) {
        String url = baseUrl + "/tickets/" + id;
        restTemplate.put(url, ticket);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        String url = baseUrl + "/tickets/" + id;
        restTemplate.delete(url);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
