package com.prgrms.nabmart.domain.category.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.exception.NoSuchCategoryException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional
    public Long saveMainCategory(RegisterMainCategoryCommand registerMainCategoryCommand) {
        String newCategoryName = registerMainCategoryCommand.name();
        if (mainCategoryRepository.existsByName(newCategoryName)) {
            throw new DuplicateCategoryNameException("이미 존재하는 카테고리입니다.");
        }
        MainCategory mainCategory = new MainCategory(newCategoryName);
        return mainCategoryRepository.save(mainCategory).getMainCategoryId();
    }

    @Transactional
    public Long saveSubCategory(RegisterSubCategoryCommand registerSubCategoryCommand) {
        String newSubCategoryName = registerSubCategoryCommand.name();
        Long parentCategoryId = registerSubCategoryCommand.mainCategoryId();
        MainCategory mainCategory = mainCategoryRepository.findById(parentCategoryId)
            .orElseThrow(() -> new NoSuchCategoryException("없는 대카테고리입니다."));

        if (subCategoryRepository.existsByMainCategoryAndName(mainCategory, newSubCategoryName)) {
            throw new DuplicateCategoryNameException("이미 존재하는 소카테고리입니다.");
        }
        SubCategory subCategory = new SubCategory(mainCategory, newSubCategoryName);
        return subCategoryRepository.save(subCategory).getSubCategoryId();
    }
}
