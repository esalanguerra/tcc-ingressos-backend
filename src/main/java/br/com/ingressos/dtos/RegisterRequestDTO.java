package br.com.ingressos.dtos;

public record RegisterRequestDTO(String email, String password, String name, String phone, String 
address, String role) {

}
