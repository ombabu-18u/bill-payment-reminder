package com.billreminder.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billreminder.system.model.BillCategory;

public interface BillCategoryRepository extends JpaRepository<BillCategory, Long> {
    
    // Custom method to check for a category by name, useful for validation
    Optional<BillCategory> findByName(String name);
}