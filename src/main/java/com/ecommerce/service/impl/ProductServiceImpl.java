package com.ecommerce.service.impl;

import com.ecommerce.common.PageResult;
import com.ecommerce.dto.AiDescriptionRequest;
import com.ecommerce.dto.ProductSaveRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.AiDescriptionHelper;
import com.ecommerce.util.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AiDescriptionHelper aiDescriptionHelper;

    @Override
    public PageResult<Product> query(String name, String category, BigDecimal minPrice, BigDecimal maxPrice,
                                     int page, int size, boolean adminView) {
        // 仅管理员且显式 adminView 时可查看已下架商品
        boolean includeDeleted = adminView && UserContext.isAdmin();
        int offset = (page - 1) * size;
        long total = productMapper.countQuery(name, category, minPrice, maxPrice, includeDeleted);
        return new PageResult<>(total, page, size,
                productMapper.queryPage(name, category, minPrice, maxPrice, includeDeleted, offset, size));
    }

    @Override
    public Product getById(Long id) {
        Product p = productMapper.findById(id);
        if (p == null || (p.getDeleted() != null && p.getDeleted() == 1)) {
            throw new BusinessException("商品不存在或已下架");
        }
        return p;
    }

    @Override
    public Product create(ProductSaveRequest request) {
        requireAdmin();
        Product product = toEntity(request);
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            product.setDescription(aiDescriptionHelper.generate(product.getName(), product.getCategory()));
        }
        productMapper.insert(product);
        return productMapper.findById(product.getId());
    }

    @Override
    public Product update(ProductSaveRequest request) {
        requireAdmin();
        if (request.getId() == null) {
            throw new BusinessException("商品ID不能为空");
        }
        getById(request.getId());
        Product product = toEntity(request);
        product.setId(request.getId());
        productMapper.update(product);
        return productMapper.findById(product.getId());
    }

    @Override
    public void delete(Long id, boolean physical) {
        requireAdmin();
        Product p = productMapper.findById(id);
        if (p == null) {
            throw new BusinessException("商品不存在");
        }
        int unfinished = productMapper.countUnfinishedOrdersByProduct(id);
        if (unfinished > 0) {
            throw new BusinessException("该商品存在未完成订单，无法删除");
        }
        if (physical) {
            productMapper.physicalDelete(id);
        } else {
            productMapper.logicalDelete(id);
        }
    }

    @Override
    public String generateDescription(AiDescriptionRequest request) {
        requireAdmin();
        return aiDescriptionHelper.generate(request.getName(), request.getCategory());
    }

    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }

    private Product toEntity(ProductSaveRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        return product;
    }
}
