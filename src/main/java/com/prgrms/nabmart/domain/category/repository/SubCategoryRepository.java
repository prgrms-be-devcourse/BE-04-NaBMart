package com.prgrms.nabmart.domain.category.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    boolean existsByMainCategoryAndName(MainCategory mainCategory, String name);

    List<SubCategory> findByMainCategory(MainCategory mainCategory);

    Optional<SubCategory> findByName(String name);
}
