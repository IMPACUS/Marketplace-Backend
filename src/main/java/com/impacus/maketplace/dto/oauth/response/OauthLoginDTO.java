package com.impacus.maketplace.dto.oauth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.Getter;

@Getter
public class OauthLoginDTO extends UserDTO {
    @JsonProperty(value = "isSignIn")
    private boolean isSignIn;

    public OauthLoginDTO(
            User user,
            boolean isSignIn,
            TokenInfoVO token
    ) {
        super(user, token);
        this.isSignIn = isSignIn;
    }
}
