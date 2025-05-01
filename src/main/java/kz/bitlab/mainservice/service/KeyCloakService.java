package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.TokenResponseDto;
import kz.bitlab.mainservice.dto.UserCreateDto;
import kz.bitlab.mainservice.dto.UserSignInDto;
import kz.bitlab.mainservice.dto.UserUpdateDto;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeyCloakService {

    UserRepresentation createUser(UserCreateDto user);

    TokenResponseDto signIn(UserSignInDto userSignInDto);

    void changePassword(String username, String newPassword);

    TokenResponseDto refreshAccessToken(String refreshToken);

    void updateUser(String currentUsername, UserUpdateDto userUpdateDto);
}
