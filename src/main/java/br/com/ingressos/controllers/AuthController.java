package br.com.ingressos.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import br.com.ingressos.dtos.AuthResponseDTO;
import br.com.ingressos.dtos.LoginRequestDTO;
import br.com.ingressos.dtos.RegisterRequestDTO;
import br.com.ingressos.infra.TokenService;
import br.com.ingressos.models.User;
import br.com.ingressos.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${service.base.url}")
    private String baseUrl;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = this.repository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new AuthResponseDTO(user.getId(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO body) {
        // Criptografar a senha
        String encodedPassword = passwordEncoder.encode(body.password());

        // Criar um novo objeto RegisterRequestDTO com a senha criptografada
        RegisterRequestDTO encryptedBody = new RegisterRequestDTO(
            body.email(),
            encodedPassword,
            body.name(),
            body.phone(),
            body.address(),
            body.role()
            );

        try {
            // Construir a URL do serviço principal usando a URL base
            String mainServiceUrl = baseUrl + "/users";
            ResponseEntity<String> response = restTemplate.postForEntity(mainServiceUrl, encryptedBody, String.class);

            // Verificar a resposta do serviço principal
            if (response.getStatusCode().is2xxSuccessful()) {
                // Extrair o publicId da resposta
                String publicId = extractPublicId(response.getBody());

                if (publicId != null) {
                    // Criar o corpo da requisição com um Map
                    Map<String, String> requestPayload = new HashMap<>();
                    requestPayload.put("userPublicId", publicId);

                    // Determinar a URL de destino com base no cargo
                    String targetUrl = determineTargetUrl(body.role());
                    if (targetUrl != null) {
                        try {
                            // Construir a URL de destino usando a URL base
                            String targetServiceUrl = baseUrl + targetUrl;
                            // Enviar o Map com publicId para a URL apropriada
                            ResponseEntity<String> roleResponse = restTemplate.postForEntity(targetServiceUrl,
                                    requestPayload,
                                    String.class);
                            return ResponseEntity.status(roleResponse.getStatusCode()).body(roleResponse.getBody());
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error sending role data");
                        }
                    } else {
                        return ResponseEntity.badRequest().body("Invalid role");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Error extracting publicId");
                }
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Error creating user");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending data to main service");
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<String> getProfile(@PathVariable String id) {
        String url = baseUrl + "/users/" + id;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
    
    private String extractPublicId(String responseBody) {
        try {
            // Converter o corpo da resposta em um JsonNode
            JsonNode rootNode = objectMapper.readTree(responseBody);
            // Extrair o valor do campo publicId
            return rootNode.path("publicId").asText(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String determineTargetUrl(String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return "/users-admins";
            case "client":
                return "/users-clients";
            case "staff":
                return "/users-staffs";
            default:
                return null;
        }
    }
}
