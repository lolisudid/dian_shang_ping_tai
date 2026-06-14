package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.dto.AiDescriptionRequest;
import com.ecommerce.dto.ProductSaveRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.AiDescriptionHelper;
import com.ecommerce.util.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final AiDescriptionHelper aiDescriptionHelper;

    public ProductServiceImpl(ProductMapper productMapper, AiDescriptionHelper aiDescriptionHelper) {
        this.productMapper = productMapper;
        this.aiDescriptionHelper = aiDescriptionHelper;
    }

    @Override
    public Page<Product> query(String name, String category, java.math.BigDecimal minPrice,
                               java.math.BigDecimal maxPrice, int page, int size, boolean adminView) {
        boolean includeDeleted = adminView && UserContext.isAdmin();
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (!includeDeleted) {
            wrapper.eq(Product::getDeleted, 0);
        }
        if (name != null && !name.isEmpty()) {
            wrapper.like(Product::getName, name);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.like(Product::getCategory, category);
        }
        if (minPrice != null) {
            wrapper.ge(Product::getPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(Product::getPrice, maxPrice);
        }
        wrapper.orderByDesc(Product::getId);
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Product getById(Long id) {
        Product p = productMapper.selectById(id);
        if (p == null || Integer.valueOf(1).equals(p.getDeleted())) {
            throw new BusinessException("product not found or removed");
        }
        return p;
    }

    @Override
    public Product create(ProductSaveRequest request) {
        requireAdmin();
        Product product = toEntity(request);
        product.setDeleted(0);
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            product.setDescription(aiDescriptionHelper.generate(product.getName(), product.getCategory()));
        }
        productMapper.insert(product);
        return productMapper.selectById(product.getId());
    }

    @Override
    public Product update(ProductSaveRequest request) {
        requireAdmin();
        if (request.getId() == null) {
            throw new BusinessException("product id required");
        }
        getById(request.getId());
        Product product = toEntity(request);
        product.setId(request.getId());
        productMapper.updateById(product);
        return productMapper.selectById(product.getId());
    }

    @Override
    public void delete(Long id, boolean physical) {
        requireAdmin();
        Product p = productMapper.selectById(id);
        if (p == null) {
            throw new BusinessException("product not found");
        }
        if (physical) {
            productMapper.deleteById(id);
        } else {
            productMapper.update(null,
                    new LambdaUpdateWrapper<Product>()
                            .eq(Product::getId, id)
                            .set(Product::getDeleted, 1));
        }
    }

    @Override
    public String generateDescription(AiDescriptionRequest request) {
        requireAdmin();
        return aiDescriptionHelper.generate(request.getName(), request.getCategory());
    }

    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "admin required");
        }
    }

    private Product toEntity(ProductSaveRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        return product;
    }
}
