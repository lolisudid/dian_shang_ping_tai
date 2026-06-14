package com.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecommerce.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品 Mapper：BaseMapper 提供内置 CRUD，自定义 SQL 处理库存扣减等。
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /** 扣减库存（带行锁条件，防止超卖） */
    @Update("UPDATE product SET stock = stock - #{quantity}, update_time = datetime('now','localtime') " +
            "WHERE id = #{id} AND stock >= #{quantity} AND deleted = 0")
    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);
}
