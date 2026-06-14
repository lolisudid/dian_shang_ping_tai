package com.ecommerce.mapper;

import com.ecommerce.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemMapper {

    List<CartItem> listByUserId(@Param("userId") Long userId);

    CartItem findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    int insert(CartItem item);

    int updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    int deleteById(@Param("id") Long id);

    int deleteByUserId(@Param("userId") Long userId);
}
