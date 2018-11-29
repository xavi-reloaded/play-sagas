package dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenData extends TokenData {
    Long refreshTokenId;
}
