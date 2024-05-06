package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.dto.coupon.request.CouponSearchDTO;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequestDTO;
import com.impacus.maketplace.dto.coupon.request.CouponUserSearchDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponseDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponCustomRepository {

    CouponUserInfoResponseDTO findByAddCouponInfo(CouponUserInfoRequestDTO request);

    Page<CouponListDTO> findAllCouponList(CouponSearchDTO couponSearchDto, Pageable pageable);


    Page<CouponUserListDTO> findAllCouponUserData(CouponUserSearchDTO couponUserSearchDto, Pageable pageable);
}
