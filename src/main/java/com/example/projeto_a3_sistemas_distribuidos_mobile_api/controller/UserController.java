package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserCreateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserResponseDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserUpdateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    // --- CREATE ---
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userDTO) {
        UserResponseDTO newUser = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(newUser);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userDTO) {
        Optional<UserResponseDTO> updatedUser = userService.updateUser(id, userDTO);
        return updatedUser.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean wasDeleted = userService.deleteUser(id);
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}