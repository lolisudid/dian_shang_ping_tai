package com.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecommerce.entity.ShopOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单 Mapper。
 */
@Mapper
public interface ShopOrderMapper extends BaseMapper<ShopOrder> {

    /** 按用户统计未完成订单中某商品的购买次数 */
    @Select("SELECT COUNT(1) FROM shop_order o INNER JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE o.user_id = #{userId} AND oi.product_id = #{productId} " +
            "AND o.status IN ('SHIPPED','COMPLETED')")
    int countPurchasedByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
