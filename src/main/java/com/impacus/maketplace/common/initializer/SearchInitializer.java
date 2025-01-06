package com.impacus.maketplace.common.initializer;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.category.dto.SearchCategoryDTO;
import com.impacus.maketplace.dto.product.dto.SearchProductDTO;
import com.impacus.maketplace.redis.service.SearchProductService;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import com.impacus.maketplace.repository.product.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchInitializer {
    private static final int PAGE_COUNT = 100;
    private final ProductRepository productRepository;
    private final SuperCategoryRepository superCategoryRepository;
    private final SearchProductService searchProductService;

    public SearchInitializer(
            ProductRepository productRepository,
            SuperCategoryRepository superCategoryRepository,
            SearchProductService searchProductService
    ) {
        this.productRepository = productRepository;
        this.superCategoryRepository = superCategoryRepository;
        this.searchProductService = searchProductService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateSearchData() {
        generateProductSearchData();
        generateSuperCategorySearchData();
        generateSubCategorySearchData();

    }

    @Transactional
    public void generateProductSearchData() {
        int page = 0;
        Slice<SearchProductDTO> products;
        try {
            do {
                products = productRepository.findAllBy(PageRequest.of(page, PAGE_COUNT));

                for (SearchProductDTO product : products) {
                    processProduct(product);
                }

                page++;
            } while (products.hasNext());
        } catch (Exception e) {
            LogUtils.error("generateProductSearchData", "Fail to generate search products", e);
        }
    }

    @Transactional
    public void processProduct(SearchProductDTO productDTO) {
        searchProductService.addSearchData(
                SearchType.PRODUCT,
                productDTO.getProductId(),
                productDTO.getName()
        );
    }

    @Transactional
    public void generateSuperCategorySearchData() {
        int page = 0;
        Slice<SearchCategoryDTO> categories;
        try {
            do {
                categories = superCategoryRepository.findSuperCategoriesBy(PageRequest.of(page, PAGE_COUNT));

                for (SearchCategoryDTO category : categories) {
                    this.processCategory(SearchType.CATEGORY, category);
                }

                page++;
            } while (categories.hasNext());
        } catch (Exception e) {
            LogUtils.error("generateProductSearchData", "Fail to generate search products", e);
        }
    }


    @Transactional
    public void generateSubCategorySearchData() {
        int page = 0;
        Slice<SearchCategoryDTO> categories;
        try {
            do {
                categories = superCategoryRepository.findSubCategoriesBy(PageRequest.of(page, PAGE_COUNT));

                for (SearchCategoryDTO category : categories) {
                    this.processCategory(SearchType.CATEGORY, category);
                }

                page++;
            } while (categories.hasNext());
        } catch (Exception e) {
            LogUtils.error("generateProductSearchData", "Fail to generate search products", e);
        }
    }

    @Transactional
    public void processCategory(SearchType type, SearchCategoryDTO categoryDTO) {
        searchProductService.addSearchData(
                type,
                categoryDTO.getCategoryId(),
                categoryDTO.getName()
        );
    }
}
