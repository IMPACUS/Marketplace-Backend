package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleSellerDTO {
    private Long id;
    private String email;
    private UserType userType;

    public SimpleSellerDTO(User user) {
        this.email = user.getEmail();
        this.id = user.getId();
        this.userType = user.getType();
    }

    public static SimpleSellerDTO toDTO(User user) {
        return new SimpleSellerDTO(user);
    }
}
