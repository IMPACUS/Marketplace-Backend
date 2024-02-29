package com.impacus.maketplace.utils;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Component
public class DataLoader {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public DataLoader(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void loadData() {
		userRepository.save(new User(StringUtils.createStrEmail("admin@email.com", OauthProviderType.NONE), passwordEncoder.encode("password1234!@#$"), "admin"));
	}
}
