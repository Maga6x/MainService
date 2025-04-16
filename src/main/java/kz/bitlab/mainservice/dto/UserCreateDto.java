package kz.bitlab.mainservice.dto;


import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateDto {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}
