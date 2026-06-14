package com.ecommerce.service;

import com.ecommerce.dto.ReviewRequest;
import com.ecommerce.entity.ProductReview;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewRequest request);

    List<ProductReview> listByProduct(Long productId);
}
