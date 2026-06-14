package com.ecommerce.mapper;

import com.ecommerce.entity.ShopOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopOrderMapper {

    ShopOrder findById(@Param("id") Long id);

    List<ShopOrder> listByUserId(@Param("userId") Long userId,
                                 @Param("status") String status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countByUserId(@Param("userId") Long userId, @Param("status") String status);

    List<ShopOrder> listAll(@Param("offset") int offset, @Param("limit") int limit);

    long countAll();

    int insert(ShopOrder order);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int countUnfinishedByProduct(@Param("productId") Long productId);

    int countPurchasedByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
