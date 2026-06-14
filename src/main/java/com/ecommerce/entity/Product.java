package com.ecommerce.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品实体，对应表 product。
 * deleted：0 正常，1 逻辑删除（下架）。
 */
@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String description;
    /** 0-未删除 1-已下架 */
    private Integer deleted;
    private String createTime;
    private String updateTime;
}
