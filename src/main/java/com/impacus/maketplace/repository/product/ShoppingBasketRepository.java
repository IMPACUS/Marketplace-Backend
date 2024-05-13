package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ShoppingBasket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Long>, ShoppingBasketCustomRepository {

    Optional<ShoppingBasket> findByProductOptionIdAndUserId(Long productOptionId, Long registerId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShoppingBasket s WHERE s.productOptionId IN :productOptions")
    void deleteByProductOptionId(List<Long> productOptions);
}
