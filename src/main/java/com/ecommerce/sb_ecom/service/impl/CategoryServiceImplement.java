package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImplement implements CategoryService {

    private List<Category> categories = new ArrayList<>();

    //ADD category repository
    @Autowired
    CategoryRepository categoryRepository;

    //Model mapper
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse findAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAnOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //lista pagina
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAnOrder);
        Page<Category> categoriesPage = categoryRepository.findAll(pageDetails);

        //Obtener todas las categorias desde la base de datos.
        List<Category> categories = categoriesPage.getContent();
        //si no hay categorias creadas aun
        if (categories.isEmpty())
            throw  new APIException("Categories not created till now");

        //Convertir las entidades `Category` a `CategoryDTO` usando `ModelMapper`.
        List<CategoryDTO>  categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category,  CategoryDTO.class) )
                .toList();

        //Crear una instancia de `CategoryResponse` para devolver la lista transformada.
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        //pasar campo de la lista
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setTotalPages(categoriesPage.getTotalElements());
        categoryResponse.setTotalElements(categoriesPage.getTotalElements());
        categoryResponse.setLastPage(categoriesPage.isLast());

        //Devolver la respuesta con las categorías en formato DTO
        return categoryResponse;
    }


    @Override
    public CategoryDTO createCatagory(CategoryDTO categoryDTO) {

        Category category = modelMapper.map(categoryDTO, Category.class);

        //existe nombre de la categoria
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDB != null) {
            throw new APIException("Category whit Name " + category.getCategoryName() + " already exists");
        }

       Category savedCategory =  categoryRepository.save(category);
        CategoryDTO categoryDTOSaved = modelMapper.map(savedCategory, CategoryDTO.class);
        return categoryDTOSaved;
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));

        categoryRepository.delete(category);

        return modelMapper.map(category, CategoryDTO.class);
    }


    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        Category saveCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);

        categoryRepository.save(category);
        return modelMapper.map(saveCategory, CategoryDTO.class);
    }
}
