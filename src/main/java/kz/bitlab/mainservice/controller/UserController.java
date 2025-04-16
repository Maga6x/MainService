package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.UserCreateDto;
import kz.bitlab.mainservice.service.KeyCloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeyCloakService keyCloakService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        keyCloakService.createUser(userCreateDto);
        return ResponseEntity.ok("User created successfully");
    }
}
