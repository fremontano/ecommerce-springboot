package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    //List<Category> findAllCategories();
    //implementamos la respuesta que esta almacenada en categoryRespone
    CategoryResponse findAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    //void createCatagory(Category category);
    CategoryDTO createCatagory(CategoryDTO categoryDTO);

    //String deleteCategory(Long categoryId);
    CategoryDTO deleteCategory(Long categoryId);


    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

}
