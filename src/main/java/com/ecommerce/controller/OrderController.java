package com.ecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.common.Result;
import com.ecommerce.dto.OrderStatusRequest;
import com.ecommerce.entity.ShopOrder;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 订单接口：用户提交/查询，管理员管理。
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public Result<ShopOrder> submit() {
        return Result.ok(orderService.submitFromCart());
    }

    @GetMapping("/admin")
    public Result<Page<ShopOrder>> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(orderService.adminList(page, size));
    }

    @GetMapping("/my")
    public Result<Page<ShopOrder>> myOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(orderService.myOrders(page, size));
    }

    @GetMapping("/{id}")
    public Result<ShopOrder> detail(@PathVariable Long id) {
        return Result.ok(orderService.detail(id));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody OrderStatusRequest request) {
        orderService.updateStatus(id, request.getStatus());
        return Result.ok("状态已更新", null);
    }
}
