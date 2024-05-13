package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.Wishlist;
import com.impacus.maketplace.repository.product.querydsl.WishlistCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>, WishlistCustomRepository {

    List<Wishlist> findByProductIdAndRegisterId(Long productId, String userId);
}
