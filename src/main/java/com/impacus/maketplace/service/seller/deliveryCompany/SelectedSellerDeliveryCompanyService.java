package com.impacus.maketplace.service.seller.deliveryCompany;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SelectedSellerDeliveryCompany;
import com.impacus.maketplace.repository.seller.deliveryCompany.SelectedSellerDeliveryCompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelectedSellerDeliveryCompanyService {
    private final SelectedSellerDeliveryCompanyRepository selectedSellerDeliveryCompanyRepository;

    /**
     * SelectedSellerDeliveryCompany 다중 저장하는 함수
     *
     * @param companies
     */
    @Transactional
    public void saveAllSelectedSellerDeliveryCompany(List<SelectedSellerDeliveryCompany> companies) {
        selectedSellerDeliveryCompanyRepository.saveAll(companies);
    }

    /**
     * SelectedSellerDeliveryCompany의 저장된 순서를 수정하는 함수
     * - 저장된 택배사의 수가 같은 경우: 순서대로 변경된 택배사 정보로 수정
     * - 저장된 택배사의 수가 적은 경우: 순서대로 변경된 택배사 정보로 수정하고 택배사 추가
     * - 저장된 택배사의 수가 많은 경우: 순서대로 변경된 택배사 정보 수정하고 나머지는 삭제 후 연결되어 있는 택배사를 [사용 안함] 으로 변경 TODO
     *
     * @param sellerDeliveryCompanyId
     * @param deliveryCompanies
     */
    @Transactional
    public void updateAllSelectedSellerDeliveryCompany(Long sellerDeliveryCompanyId, List<DeliveryCompany> deliveryCompanies) {
        List<SelectedSellerDeliveryCompany> savedCompanies = selectedSellerDeliveryCompanyRepository.findBySellerDeliveryCompanyIdOrderByDisplayOrder(sellerDeliveryCompanyId);
        if (savedCompanies.size() == deliveryCompanies.size()) {
            // 1. 같은 경우, 저장된 정보 수정
            updateSavedSelectedSellerDeliveryCompanies(savedCompanies, deliveryCompanies);
        } else if (savedCompanies.size() < deliveryCompanies.size()) {
            // 2. 저장된 수가 더 작은 경우
            // 2-1. 저장된 정보 수정
            updateSavedSelectedSellerDeliveryCompanies(savedCompanies, deliveryCompanies);

            // 2-2 새로운 데이터 생성
            List<DeliveryCompany> newDeliveryCompanies = deliveryCompanies.subList(savedCompanies.size(), deliveryCompanies.size());
            List<SelectedSellerDeliveryCompany> newCompanies = new ArrayList<>();
            for (int i = 0; i < newDeliveryCompanies.size(); i++) {
                SelectedSellerDeliveryCompany company = new SelectedSellerDeliveryCompany(
                        sellerDeliveryCompanyId, newDeliveryCompanies.get(i), i + savedCompanies.size()
                );
                newCompanies.add(company);
            }
            saveAllSelectedSellerDeliveryCompany(newCompanies);
        } else {
            // 3. 저장된 수가 더 큰 경우
            // 3-1 저장된 정보 수정
            updateSavedSelectedSellerDeliveryCompanies(savedCompanies, deliveryCompanies);

            // 3-2 삭제필요 데이터 삭제
            List<SelectedSellerDeliveryCompany> deleteCompanies = savedCompanies.subList(deliveryCompanies.size(), savedCompanies.size());
            selectedSellerDeliveryCompanyRepository.deleteAll(deleteCompanies);

            // 3-2 TODO 기존에 연결되어 있던 택배사 [사용안함]으로 변경
        }
    }

    /**
     * savedCompanies 를 변경요청받은 정보로 수정
     *
     * @param savedCompanies
     * @param changedCompanies
     */
    private void updateSavedSelectedSellerDeliveryCompanies(List<SelectedSellerDeliveryCompany> savedCompanies, List<DeliveryCompany> changedCompanies) {
        for (int i = 0; i < savedCompanies.size(); i++) {
            if (i >= changedCompanies.size()) {
                break;
            }

            SelectedSellerDeliveryCompany savedCompany = savedCompanies.get(i);
            selectedSellerDeliveryCompanyRepository.updateDeliveryCompanyById(savedCompany.getId(), changedCompanies.get(i));
        }
    }
}
