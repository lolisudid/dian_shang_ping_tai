package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.dto.ReviewRequest;
import com.ecommerce.entity.ProductReview;
import com.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Result<Void> add(@Validated @RequestBody ReviewRequest request) {
        reviewService.addReview(request);
        return Result.ok("评价成功", null);
    }

    @GetMapping("/product/{productId}")
    public Result<List<ProductReview>> list(@PathVariable Long productId) {
        return Result.ok(reviewService.listByProduct(productId));
    }
}
