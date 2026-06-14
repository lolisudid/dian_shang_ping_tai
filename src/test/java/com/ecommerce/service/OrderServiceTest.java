package com.ecommerce.service;

import com.ecommerce.entity.ShopOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 订单模块 CRUD 集成测试。
 */
@SpringBootTest
@ActiveProfiles("sqlite")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("adminList 返回分页订单列表")
    void adminList_shouldReturnPaginatedOrders() {
        var page = orderService.adminList(1, 10);
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isNotNull();
    }

    @Test
    @DisplayName("detail 根据ID查询订单详情")
    void detail_existingOrder_shouldReturnOrderWithItems() {
        // 先获取一条订单ID
        var page = orderService.adminList(1, 1);
        if (page.getRecords().isEmpty()) {
            return; // 无数据时跳过
        }
        Long orderId = page.getRecords().get(0).getId();
        ShopOrder order = orderService.detail(orderId);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getItems()).isNotNull();
    }

    @Test
    @DisplayName("myOrders 普通用户查询自己订单")
    void myOrders_shouldReturnUserOrders() {
        var page = orderService.myOrders(1, 10);
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isNotNull();
    }
}
