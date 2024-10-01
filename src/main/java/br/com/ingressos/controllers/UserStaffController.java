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
@RequestMapping("/user-staff")
@RequiredArgsConstructor
public class UserStaffController {
    private final RestTemplate restTemplate;

    @Value("${service.base.url}")
    private String baseUrl;

    @GetMapping()
    public ResponseEntity<String> getUserStaffs() {
        String url = baseUrl + "/users-staffs";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserStaffById(@PathVariable Long id) {
        String url = baseUrl + "/users-staffs/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<String> getUserStaffByAdminId(@PathVariable String id) {
        String url = baseUrl + "/users-staffs/admin/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<String> createUserStaff(@RequestBody JsonNode userStaff) {
        String url = baseUrl + "/users-staffs";
        String response = restTemplate.postForObject(url, userStaff, String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserStaff(@PathVariable Long id, @RequestBody JsonNode userStaff) {
        String url = baseUrl + "/users-staffs/" + id;
        restTemplate.put(url, userStaff);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserStaff(@PathVariable Long id) {
        String url = baseUrl + "/users-staffs/" + id;
        restTemplate.delete(url);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
