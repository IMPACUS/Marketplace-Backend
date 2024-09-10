package com.impacus.maketplace.dto.user.response;

import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String email,
        String password,
        String name,
        TokenInfoVO token) {

    public UserDTO(User user, TokenInfoVO token) {
        this(user.getId(), user.getEmail(), user.getPassword(), user.getName(), token);
    }

    public UserDTO(AdminInfo admin, TokenInfoVO token) {
        this(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getName(), token);
    }

    public UserDTO(User user) {
        this(user, null);
    }
}
