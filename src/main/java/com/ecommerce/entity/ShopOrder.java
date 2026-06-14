package com.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单主表，对应 shop_order。
 * status：PENDING/SHIPPED/COMPLETED/CANCELLED
 */
@Data
@TableName("shop_order")
public class ShopOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private String createTime;
    private String updateTime;

    /** 订单明细（查询时填充） */
    @TableField(exist = false)
    private List<OrderItem> items;
    /** 用户名（管理端展示） */
    @TableField(exist = false)
    private String username;
}
