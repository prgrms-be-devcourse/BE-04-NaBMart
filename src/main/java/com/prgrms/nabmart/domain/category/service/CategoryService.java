package com.prgrms.nabmart.domain.category.service;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;

    @Transactional
    public Long saveMainCategory(RegisterMainCategoryCommand registerMainCategoryCommand) {
        String newCategoryName = registerMainCategoryCommand.name();
        if (mainCategoryRepository.existsByName(newCategoryName)) {
            throw new DuplicateCategoryNameException("이미 존재하는 카테고리입니다.");
        }
        MainCategory mainCategory = new MainCategory(newCategoryName);
        MainCategory savedMainCategory = mainCategoryRepository.save(mainCategory);
        return savedMainCategory.getMainCategoryId();
    }
}
