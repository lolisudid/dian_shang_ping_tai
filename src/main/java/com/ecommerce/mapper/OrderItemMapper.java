package com.ecommerce.mapper;

import com.ecommerce.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);

    int insertBatch(@Param("items") List<OrderItem> items);
}
