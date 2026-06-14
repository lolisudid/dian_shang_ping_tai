package com.ecommerce.mapper;

import com.ecommerce.entity.ShopOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 Mapper：支持用户分页查询、管理员分页查询。
 */
@Mapper
public interface ShopOrderMapper {

    ShopOrder findById(@Param("id") Long id);

    List<ShopOrder> listByUserId(@Param("userId") Long userId,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countByUserId(@Param("userId") Long userId);

    List<ShopOrder> listAll(@Param("offset") int offset, @Param("limit") int limit);

    long countAll();

    int insert(ShopOrder order);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int countUnfinishedByProduct(@Param("productId") Long productId);

    int countPurchasedByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
