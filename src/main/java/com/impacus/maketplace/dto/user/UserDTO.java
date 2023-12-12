package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.Data;

@Data
public class UserDTO {

    private String email;
    private String password;
    private String name;
    private TokenInfoVO token;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }

    public UserDTO(User user, TokenInfoVO token) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.token = token;
    }

}
