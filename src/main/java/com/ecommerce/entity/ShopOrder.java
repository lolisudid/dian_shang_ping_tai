package com.ecommerce.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单主表，对应 shop_order（避免 SQL 关键字 order）。
 * status：PENDING/SHIPPED/COMPLETED/CANCELLED
 */
@Data
public class ShopOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private String createTime;
    private String updateTime;

    /** 订单明细（查询时填充） */
    private List<OrderItem> items;
    /** 用户名（管理端展示） */
    private String username;
}
