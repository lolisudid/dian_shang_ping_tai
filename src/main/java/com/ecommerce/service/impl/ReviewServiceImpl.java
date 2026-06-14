package com.ecommerce.service.impl;

import com.ecommerce.dto.ReviewRequest;
import com.ecommerce.entity.ProductReview;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.ProductReviewMapper;
import com.ecommerce.mapper.ShopOrderMapper;
import com.ecommerce.service.ReviewService;
import com.ecommerce.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;
    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Override
    public void addReview(ReviewRequest request) {
        Long userId = UserContext.currentUserId();
        int purchased = shopOrderMapper.countPurchasedByUserAndProduct(userId, request.getProductId());
        if (purchased == 0) {
            throw new BusinessException("仅可对已购商品评价");
        }
        if (productReviewMapper.findByUserAndOrderAndProduct(userId, request.getOrderId(), request.getProductId()) != null) {
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
        return productReviewMapper.listByProductId(productId);
    }
}
