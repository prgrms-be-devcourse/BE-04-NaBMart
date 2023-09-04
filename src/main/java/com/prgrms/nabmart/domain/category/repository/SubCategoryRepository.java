package com.prgrms.nabmart.domain.category.repository;

import com.prgrms.nabmart.domain.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    boolean existsByName(String name);
}
