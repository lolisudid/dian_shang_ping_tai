package com.ecommerce;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 电商购物平台启动类。
 * @MapperScan 扫描 MyBatis Mapper 接口，无需在每个 Mapper 上重复标注（接口上仍保留 @Mapper 双保险）。
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.ecommerce.mapper")
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
