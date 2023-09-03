package com.prgrms.nabmart.domain.category.repository;

import com.prgrms.nabmart.domain.category.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {

    boolean existsByName(String name);
}
