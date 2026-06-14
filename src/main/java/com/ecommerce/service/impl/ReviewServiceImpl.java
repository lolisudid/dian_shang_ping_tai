package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecommerce.dto.ReviewRequest;
import com.ecommerce.entity.ProductReview;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.ProductReviewMapper;
import com.ecommerce.mapper.ShopOrderMapper;
import com.ecommerce.service.ReviewService;
import com.ecommerce.util.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评价服务实现。
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ProductReviewMapper productReviewMapper;
    private final ShopOrderMapper shopOrderMapper;

    public ReviewServiceImpl(ProductReviewMapper productReviewMapper, ShopOrderMapper shopOrderMapper) {
        this.productReviewMapper = productReviewMapper;
        this.shopOrderMapper = shopOrderMapper;
    }

    @Override
    public void addReview(ReviewRequest request) {
        Long userId = UserContext.currentUserId();
        int purchased = shopOrderMapper.countPurchasedByUserAndProduct(userId, request.getProductId());
        if (purchased == 0) {
            throw new BusinessException("仅可对已购商品评价");
        }
        if (productReviewMapper.selectCount(new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getUserId, userId)
                .eq(ProductReview::getOrderId, request.getOrderId())
                .eq(ProductReview::getProductId, request.getProductId())) > 0) {
            throw new BusinessException("该订单商品已评价");
        }
        ProductReview review = new ProductReview();
        review.setUserId(userId);
        review.setProductId(request.getProductId());
        review.setOrderId(request.getOrderId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        productReviewMapper.insert(review);
    }

    @Override
    public List<ProductReview> listByProduct(Long productId) {
        return productReviewMapper.selectList(
                new LambdaQueryWrapper<ProductReview>()
                        .eq(ProductReview::getProductId, productId)
                        .orderByDesc(ProductReview::getId));
    }
}
