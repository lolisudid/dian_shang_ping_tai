package com.ecommerce.mapper;

import com.ecommerce.entity.ProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductReviewMapper {

    int insert(ProductReview review);

    List<ProductReview> listByProductId(@Param("productId") Long productId);

    ProductReview findByUserAndOrderAndProduct(@Param("userId") Long userId,
                                               @Param("orderId") Long orderId,
                                               @Param("productId") Long productId);
}
