package kz.bitlab.mainservice.service;

import jakarta.ws.rs.core.Response;
import kz.bitlab.mainservice.dto.TokenResponseDto;
import kz.bitlab.mainservice.dto.UserCreateDto;
import kz.bitlab.mainservice.dto.UserSignInDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyCloakService {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;

    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.client-secret}")
    private String clientSecret;


    public UserRepresentation createUser(UserCreateDto user) {

        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(user.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);
        newUser.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(newUser);

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.error("Error creating user");
            throw new RuntimeException("Failed creating user");
        }

        List<UserRepresentation> searchUsers = keycloak.realm(realm).users().search(user.getUsername());
        if (searchUsers.isEmpty()) {
            throw new RuntimeException("User created but not found");
        }

        String userId = searchUsers.get(0).getId();

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<org.keycloak.representations.idm.RoleRepresentation> keycloakRoles = user.getRoles().stream()
                    .map(roleName -> keycloak.realm(realm).roles().get(roleName).toRepresentation())
                    .toList();

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(keycloakRoles);

            log.info("Assigned roles {} to user {}", user.getRoles(), user.getUsername());
        }

        return searchUsers.get(0);
    }

    public TokenResponseDto signIn(UserSignInDto userSignInDto) {
        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", client);
        formData.add("client_secret", clientSecret);
        formData.add("username", userSignInDto.getUsername());
        formData.add("password", userSignInDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, new HttpEntity<>(formData, headers), Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error signIn");
            throw new RuntimeException("Failed signIn");
        }

        return new TokenResponseDto(
                (String) responseBody.get("access_token"),
                (String) responseBody.get("refresh_token"),
                (Integer) responseBody.get("expires_in")
        );
    }


    public void changePassword(String username, String newPassword) {

        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username);
        if (users.isEmpty()) {
            log.error("User not found to change password");
            throw new RuntimeException("User not found with username " + username);
        }

        UserRepresentation userRepresentation = users.get(0);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credentialRepresentation);
        log.info("Changed password");

    }
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", client);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                tokenEndpoint,
                new HttpEntity<>(formData, headers),
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error refreshing token: {}", response.getStatusCode());
            throw new RuntimeException("Failed to refresh access token");
        }

        return new TokenResponseDto(
                (String) responseBody.get("access_token"),
                (String) responseBody.get("refresh_token"),
                (Integer) responseBody.get("expires_in")
        );
    }
}