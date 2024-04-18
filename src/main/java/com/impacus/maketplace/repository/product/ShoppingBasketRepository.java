package com.impacus.maketplace.repository.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.product.ShoppingBasket;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Long>, ShoppingBasketCustomRepository {

    Optional<ShoppingBasket> findByProductOptionIdAndUserId(Long productOptionId, String registerId);
}
