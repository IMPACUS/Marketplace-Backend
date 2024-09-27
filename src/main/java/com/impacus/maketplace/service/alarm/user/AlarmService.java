package com.impacus.maketplace.service.alarm.user;

import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.*;
import com.impacus.maketplace.entity.alarm.user.*;
import com.impacus.maketplace.entity.alarm.user.enums.*;
import com.impacus.maketplace.repository.alarm.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    private final AlarmBrandShopRepository alarmBrandShopRepository;
    private final AlarmShoppingBenefitsRepository alarmShoppingBenefitsRepository;
    private final AlarmOrderDeliveryRepository alarmOrderDeliveryRepository;
    private final AlarmRestockRepository alarmRestockRepository;
    private final AlarmReviewRepository alarmReviewRepository;
    private final AlarmServiceCenterRepository alarmServiceCenterRepository;

    public void add(Object saveDto, Long userId) {
        if (saveDto instanceof AddOrderDeliveryDto) {
            AddOrderDeliveryDto orderDeliveryDto = (AddOrderDeliveryDto) saveDto;
            alarmOrderDeliveryRepository.save(orderDeliveryDto.toEntity(userId));
        } else if (saveDto instanceof AddBrandShopDto) {
            AddBrandShopDto brandShopDto = (AddBrandShopDto) saveDto;
            alarmBrandShopRepository.save(brandShopDto.toEntity(userId));
        } else if (saveDto instanceof AddReviewDto) {
            AddReviewDto reviewDto = (AddReviewDto) saveDto;
            alarmReviewRepository.save(reviewDto.toEntity(userId));
        } else if (saveDto instanceof AddRestockDto) {
            AddRestockDto restockDto = (AddRestockDto) saveDto;
            alarmRestockRepository.save(restockDto.toEntity(userId));
        } else if (saveDto instanceof AddShoppingBenefitsDto) {
            AddShoppingBenefitsDto shoppingBenefitsDto = (AddShoppingBenefitsDto) saveDto;
            alarmShoppingBenefitsRepository.save(shoppingBenefitsDto.toEntity(userId));
        } else if (saveDto instanceof AddServiceCenterDto) {
            AddServiceCenterDto serviceCenterDto = (AddServiceCenterDto) saveDto;
            alarmServiceCenterRepository.save(serviceCenterDto.toEntity(userId));
        } else {
            throw new IllegalArgumentException("존재하지 않는 DTO입니다.");
        }
    }

    @Transactional(readOnly = true)
    public Object find(Object content, Long userId) {
        if (content instanceof OrderDeliveryEnum) {
            OrderDeliveryEnum orderDelivery = (OrderDeliveryEnum) content;
            return alarmOrderDeliveryRepository.findData(orderDelivery, userId);
        } else if (content instanceof BrandShopEnum) {
            BrandShopEnum brandShop = (BrandShopEnum) content;
            return alarmBrandShopRepository.findData(brandShop, userId);
        } else if (content instanceof ReviewEnum) {
            ReviewEnum review = (ReviewEnum) content;
            return alarmReviewRepository.findData(review, userId);
        } else if (content instanceof RestockEnum) {
            RestockEnum restock = (RestockEnum) content;
            return alarmRestockRepository.findData(restock, userId);
        } else if (content instanceof ShoppingBenefitsEnum) {
            ShoppingBenefitsEnum shoppingBenefits = (ShoppingBenefitsEnum) content;
            return alarmShoppingBenefitsRepository.findData(shoppingBenefits, userId);
        } else if (content instanceof ServiceCenterEnum) {
            ServiceCenterEnum serviceCenter = (ServiceCenterEnum) content;
            return alarmServiceCenterRepository.findData(serviceCenter, userId);
        } else {
            throw new IllegalArgumentException("존재하지 않는 Enum입니다.");
        }
    }

    public void update(Long id, Object updateDto, Long userId) {
        if (updateDto instanceof UpdateBrandShopDto) {
            UpdateBrandShopDto updateBrandShopDto = (UpdateBrandShopDto) updateDto;
            Optional<AlarmBrandShop> byId = alarmBrandShopRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmBrandShop alarmBrandShop = byId.get();
            if (alarmBrandShop.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmBrandShop.updateAlarm(updateBrandShopDto);
        } else if (updateDto instanceof UpdateOrderDeliveryDto) {
            UpdateOrderDeliveryDto updateOrderDeliveryDto = (UpdateOrderDeliveryDto) updateDto;
            Optional<AlarmOrderDelivery> byId = alarmOrderDeliveryRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmOrderDelivery alarmOrderDelivery = byId.get();
            if (alarmOrderDelivery.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmOrderDelivery.updateAlarm(updateOrderDeliveryDto);
        } else if (updateDto instanceof UpdateRestockDto) {
            UpdateRestockDto updateRestockDto = (UpdateRestockDto) updateDto;
            Optional<AlarmRestock> byId = alarmRestockRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmRestock alarmRestock = byId.get();
            if (alarmRestock.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmRestock.updateAlarm(updateRestockDto);
        } else if (updateDto instanceof UpdateReviewDto) {
            UpdateReviewDto updateReviewDto = (UpdateReviewDto) updateDto;
            Optional<AlarmReview> byId = alarmReviewRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmReview alarmReview = byId.get();
            if (alarmReview.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmReview.updateAlarm(updateReviewDto);
        } else if (updateDto instanceof UpdateShoppingBenefitsDto) {
            UpdateShoppingBenefitsDto updateShoppingBenefitsDto = (UpdateShoppingBenefitsDto) updateDto;
            Optional<AlarmShoppingBenefits> byId = alarmShoppingBenefitsRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmShoppingBenefits alarmShoppingBenefits = byId.get();
            if (alarmShoppingBenefits.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmShoppingBenefits.updateAlarm(updateShoppingBenefitsDto);
        } else if (updateDto instanceof UpdateServiceCenterDto) {
            UpdateServiceCenterDto updateServiceCenterDto = (UpdateServiceCenterDto) updateDto;
            Optional<AlarmServiceCenter> byId = alarmServiceCenterRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmServiceCenter alarmServiceCenter = byId.get();
            if (alarmServiceCenter.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmServiceCenter.updateAlarm(updateServiceCenterDto);
        } else {
            throw new IllegalArgumentException("존재하지 않는 Dto입니다.");
        }
    }
}
