package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.dto.ReviewRequest;
import com.ecommerce.entity.ProductReview;
import com.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价接口。
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Result<Void> add(@Valid @RequestBody ReviewRequest request) {
        reviewService.addReview(request);
        return Result.ok("评价成功", null);
    }

    @GetMapping("/product/{productId}")
    public Result<List<ProductReview>> list(@PathVariable Long productId) {
        return Result.ok(reviewService.listByProduct(productId));
    }
}
