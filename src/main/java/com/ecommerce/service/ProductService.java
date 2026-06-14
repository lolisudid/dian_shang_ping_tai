package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.dto.AiDescriptionRequest;
import com.ecommerce.dto.ProductSaveRequest;
import com.ecommerce.entity.Product;

import java.math.BigDecimal;

public interface ProductService {

    Page<Product> query(String name, String category, BigDecimal minPrice, BigDecimal maxPrice,
                        int page, int size, boolean adminView);

    Product getById(Long id);

    Product create(ProductSaveRequest request);

    Product update(ProductSaveRequest request);

    void delete(Long id, boolean physical);

    String generateDescription(AiDescriptionRequest request);
}