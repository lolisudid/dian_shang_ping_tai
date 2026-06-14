package com.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车项，对应表 cart_item。
 */
@Data
@TableName("cart_item")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String createTime;

    /** 关联查询字段（非表字段） */
    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private BigDecimal productPrice;
    @TableField(exist = false)
    private String productImageUrl;
    @TableField(exist = false)
    private Integer productStock;
}
