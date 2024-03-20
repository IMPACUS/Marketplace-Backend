package com.impacus.maketplace.common.initializer;

import com.impacus.maketplace.entity.category.SubCategory;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.repository.category.SubCategoryRepository;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryInitializer {
    private final SuperCategoryRepository superCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryInitializer(SuperCategoryRepository superCategoryRepository, SubCategoryRepository subCategoryRepository) {
        this.superCategoryRepository = superCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyCategoryData() {
        SuperCategory category1 = new SuperCategory("브랜드");
        SuperCategory category2 = new SuperCategory("패션");
        SuperCategory category3 = new SuperCategory("가방");
        SuperCategory category4 = new SuperCategory("악세서리");
        SuperCategory category5 = new SuperCategory("가전/가구");
        SuperCategory category6 = new SuperCategory("라이프 스타일");
        SuperCategory category7 = new SuperCategory("ECO");

        List<SuperCategory> superCategories = List.of(category1, category2, category3, category4, category5, category6, category7)
                .stream()
                .filter(superCategory -> !superCategoryRepository.existsByName(superCategory.getName()))
                .toList();

        if (superCategories.size() > 0) {
            superCategoryRepository.saveAll(superCategories);


            for (SuperCategory superCategory : superCategories) {
                List<String> subCategoryNames = new ArrayList<>();

                switch (superCategory.getName()) {
                    case "패션": {
                        subCategoryNames = List.of("T-shirt", "바지", "셔츠", "신발", "모자", "자켓", "코트", "잠바", "스카프/머플러", "니트/가디건", "비치웨어", "장갑", "벨트", "양말", "스타킹");
                    }
                    break;
                    case "가방": {
                        subCategoryNames = List.of("크로스백", "토트백", "숄더백", "클러치", "에코백", "배낭", "캐리어", "지갑", "파우치");
                    }
                    break;
                    case "악세서리": {
                        subCategoryNames = List.of("귀걸이", "목걸이", "팔찌", "피어싱", "반지", "머리띠", "머리끈", "헤어클립", "시계", "발찌", "안경", "애플 케이스", "삼성 케이스");
                    }
                    break;
                    case "가전/가구": {
                        subCategoryNames = List.of("스킨케어", "선케어", "화장품", "립", "클렌징", "마스크/팩", "바디", "네일", "로션", "헤어", "향수", "미스트", "스크럽");
                    }
                    break;
                    case "라이프 스타일": {
                        subCategoryNames = List.of("세제", "청소 용품", "식기세척기", "탁자", "식탁", "책상", "의자", "수납장", "거울", "서랍", "침대", "쇼파", "커튼", "휴지", "수건");
                    }
                    break;
                    case "ECO": {
                        subCategoryNames = List.of("캠핑", "아웃도어", "스포츠", "생활용품", "생필품", "스킨케어", "포장재", "완충재", "박스", "종이", "가방", "생활용품", "패션", "생필품", "핸드메이드", "반려동물 용품", "에코백", "휴지");
                    }
                    case "브랜드": {
                        continue;
                    }
                }

                List<SubCategory> subCategories = subCategoryNames.stream().map(
                        subCategoryName -> new SubCategory(superCategory.getId(), null, subCategoryName)
                ).toList();

                subCategoryRepository.saveAll(subCategories);
            }
        }
    }
}
