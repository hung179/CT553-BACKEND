package com.ecommerce.studentmarket.product.category.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.product.category.domains.CategoryDomain;
import com.ecommerce.studentmarket.product.category.dtos.CategoryRequestDto;
import com.ecommerce.studentmarket.product.category.dtos.CategoryResponseDto;
import com.ecommerce.studentmarket.product.category.exceptions.CategoryAlreadyExistsException;
import com.ecommerce.studentmarket.product.category.exceptions.CategoryNotFoundException;
import com.ecommerce.studentmarket.product.category.repositories.CategoryRepository;
import com.ecommerce.studentmarket.product.item.domains.ProductDomain;
import com.ecommerce.studentmarket.product.item.repositories.ProductRepository;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ProductRepository productRepository;

    public CategoryResponseDto getCategoryById (Long maDM) {
         CategoryDomain category = categoryRepository.findById(maDM).orElseThrow(
                () -> new CategoryNotFoundException(maDM)
        );
        return convertToCategoryDtoRespone(category);
    }

    public Page<CategoryResponseDto> getAllCategory (Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDomain> category = categoryRepository.findAll(pageable);
        return category.map(this::convertToCategoryDtoRespone);
    }

    public ApiResponse createCategory (CategoryRequestDto categoryRequestDto) {
        boolean exists = categoryRepository.existsByTenDMContainingIgnoreCase(categoryRequestDto.getTenDM());
        if(exists){
            throw new CategoryAlreadyExistsException(categoryRequestDto.getTenDM());
        }

        CategoryDomain categoryDomain = new CategoryDomain();

        categoryDomain.setTenDM(categoryRequestDto.getTenDM());

        categoryRepository.save(categoryDomain);

        return new ApiResponse("Thêm danh mục thành công", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse updateCategory (Long maDM, CategoryRequestDto categoryRequestDto){
        CategoryDomain category = categoryRepository.findById(maDM).orElseThrow(
                () -> new CategoryNotFoundException(maDM)
        );


        Optional.ofNullable(categoryRequestDto.getTenDM()).ifPresent(category::setTenDM);
        System.out.println(categoryRequestDto.getTenDM());
        categoryRepository.save(category);

        return new ApiResponse("Cập nhật danh mục thành công", true, ApiResponseType.SUCCESS);
    }

    @Transactional
    public ApiResponse deleteCategory (Long maDM){
        CategoryDomain category = categoryRepository.findByIdWithProducts(maDM)
                .orElseThrow(() -> new CategoryNotFoundException(maDM));

        for (ProductDomain product : category.getProducts()) {
            product.setCategory(null);
            productRepository.save(product);
        }
        categoryRepository.delete(category);

        return new ApiResponse("Xóa danh mục thành công", true, ApiResponseType.SUCCESS);
    }

    private CategoryResponseDto convertToCategoryDtoRespone(CategoryDomain categoryDomain) {
        CategoryResponseDto category = new CategoryResponseDto();

        Optional.of(categoryDomain.getMaDM()).ifPresent(category::setMaDM);
        Optional.ofNullable(categoryDomain.getTenDM()).ifPresent(category::setTenDM);

        return category;
    }
}
