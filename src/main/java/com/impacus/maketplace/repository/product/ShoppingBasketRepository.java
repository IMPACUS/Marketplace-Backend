package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Long>, ShoppingBasketCustomRepository {

    Optional<ShoppingBasket> findByProductOptionIdAndUserId(Long productOptionId, Long registerId);
}
