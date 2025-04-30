package kz.bitlab.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
}
