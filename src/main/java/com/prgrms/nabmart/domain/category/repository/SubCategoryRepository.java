package com.prgrms.nabmart.domain.category.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    boolean existsByMainCategoryAndName(final MainCategory mainCategory, final String name);

    List<SubCategory> findByMainCategory(final MainCategory mainCategory);
}
