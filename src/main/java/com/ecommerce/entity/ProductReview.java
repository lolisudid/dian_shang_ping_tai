package com.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品评价，仅允许对已购商品评价。
 */
@Data
@TableName("product_review")
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    private Long orderId;
    /** 评分 1-5 */
    private Integer rating;
    private String content;
    private String createTime;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String productName;
}
