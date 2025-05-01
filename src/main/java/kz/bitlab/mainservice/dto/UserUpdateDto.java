package kz.bitlab.mainservice.dto;


import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;
}
