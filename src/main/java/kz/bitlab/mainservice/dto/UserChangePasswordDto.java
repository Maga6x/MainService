package kz.bitlab.mainservice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserChangePasswordDto {

    private String newPassword;
}
