package com.ecommerce.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品评价，仅允许对已购商品评价。
 */
@Data
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long productId;
    private Long orderId;
    /** 评分 1-5 */
    private Integer rating;
    private String content;
    private String createTime;

    private String username;
    private String productName;
}
