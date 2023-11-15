package com.impacus.maketplace.entity.dto.user;

import com.impacus.maketplace.entity.User;
import lombok.Data;

@Data
public class UserDTO {

    private String email;
    private String password;
    private String name;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }

}
