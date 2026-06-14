package com.ecommerce.mapper;

import com.ecommerce.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProductMapper {

    Product findById(@Param("id") Long id);

    List<Product> queryPage(@Param("name") String name,
                            @Param("category") String category,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice,
                            @Param("includeDeleted") boolean includeDeleted,
                            @Param("offset") int offset,
                            @Param("limit") int limit);

    long countQuery(@Param("name") String name,
                    @Param("category") String category,
                    @Param("minPrice") BigDecimal minPrice,
                    @Param("maxPrice") BigDecimal maxPrice,
                    @Param("includeDeleted") boolean includeDeleted);

    int insert(Product product);

    int update(Product product);

    int logicalDelete(@Param("id") Long id);

    int physicalDelete(@Param("id") Long id);

    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    int countUnfinishedOrdersByProduct(@Param("productId") Long productId);
}
