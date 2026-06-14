package com.ecommerce.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 购物车项，对应表 cart_item。
 */
@Data
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String createTime;

    /** 关联查询：商品名称（非表字段） */
    private String productName;
    private java.math.BigDecimal productPrice;
    private String productImageUrl;
    private Integer productStock;
}
