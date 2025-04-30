package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.TokenResponseDto;
import kz.bitlab.mainservice.dto.UserChangePasswordDto;
import kz.bitlab.mainservice.dto.UserCreateDto;
import kz.bitlab.mainservice.dto.UserSignInDto;
import kz.bitlab.mainservice.service.KeyCloakService;
import kz.bitlab.mainservice.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeyCloakService keyCloakService;

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto) {
        return keyCloakService.createUser(userCreateDto);
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userSignInDto) {
        return ResponseEntity.ok(keyCloakService.signIn(userSignInDto));
    }

    @PostMapping(value = "/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {
        String currentUserName = UserUtils.getCurrentUser();
        if (currentUserName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn`t Identity User");
        }
        try {
            keyCloakService.changePassword(currentUserName, userChangePasswordDto.getNewPassword());
            return ResponseEntity.ok("Password changed");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        return ResponseEntity.ok(keyCloakService.refreshAccessToken(refreshToken));
    }
}
