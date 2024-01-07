package security;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.entity.user.User;
import lombok.Getter;

@Getter
public class SessionUser {

    private String name;
    private String email;
    private UserType role;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getType();
    }
}
