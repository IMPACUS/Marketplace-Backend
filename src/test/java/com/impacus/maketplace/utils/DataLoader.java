package com.impacus.maketplace.utils;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
import com.impacus.maketplace.repository.product.ProductRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Component
public class DataLoader {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ProductRepository productRepository;
	private ProductOptionRepository productOptionRepository;

	public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.productRepository = productRepository;
		this.productOptionRepository = productOptionRepository;
	}

	public void loadData() {
		userRepository.save(new User(StringUtils.createStrEmail("admin@email.com", OauthProviderType.NONE), passwordEncoder.encode("password1234!@#$"), "admin"));
		productRepository.save(new Product(1L, 1L, "아디다스 슈퍼스타","1", DeliveryType.GENERAL_DELIVERY, SubCategory.SHOE, 3000, 3000, 10000, 10000, 8000, 100, false, ProductStatus.SALES_PROGRESS, DiscountStatus.DISCOUNT_PROGRESS));
		productOptionRepository.save(new ProductOption(1L, 1L, "blue", "230", 200L, false));
		productOptionRepository.save(new ProductOption(2L, 1L, "black", "240", 180L, false));
	}
}
