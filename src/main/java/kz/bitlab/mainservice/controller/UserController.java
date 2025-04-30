package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.UserCreateDto;
import kz.bitlab.mainservice.dto.UserSignInDto;
import kz.bitlab.mainservice.service.KeyCloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeyCloakService keyCloakService;

    @PostMapping(value = "/create")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto) {
        return keyCloakService.createUser(userCreateDto);
    }

    @PostMapping("/sign-in")
    public String signIn(@RequestBody UserSignInDto userSignInDto) {
        return keyCloakService.signIn(userSignInDto);
    }
}
