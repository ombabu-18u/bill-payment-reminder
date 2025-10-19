package com.billreminder.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billreminder.system.exception.DuplicateResourceException;
import com.billreminder.system.exception.ResourceNotFoundException;
import com.billreminder.system.model.BillCategory;
import com.billreminder.system.repository.BillCategoryRepository;

@Service
public class BillCategoryService {

    private final BillCategoryRepository categoryRepository;

    @Autowired
    public BillCategoryService(BillCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public BillCategory createCategory(BillCategory category) {
        // Validation: Check for duplicate name
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DuplicateResourceException("BillCategory", "name", category.getName());
        }
        return categoryRepository.save(category);
    }

    public List<BillCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public BillCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BillCategory", "Id", id));
    }
}