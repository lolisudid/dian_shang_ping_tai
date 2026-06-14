package com.ecommerce.controller;

import com.ecommerce.common.PageResult;
import com.ecommerce.common.Result;
import com.ecommerce.dto.OrderStatusRequest;
import com.ecommerce.entity.ShopOrder;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<ShopOrder> submit() {
        return Result.ok(orderService.submitFromCart());
    }

    @GetMapping("/admin")
    public Result<PageResult<ShopOrder>> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(orderService.adminList(page, size));
    }

    @GetMapping("/my")
    public Result<PageResult<ShopOrder>> myOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return Result.ok(orderService.myOrders(page, size, status));
    }

    @GetMapping("/{id}")
    public Result<ShopOrder> detail(@PathVariable Long id) {
        return Result.ok(orderService.detail(id));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @Validated @RequestBody OrderStatusRequest request) {
        orderService.updateStatus(id, request.getStatus());
        return Result.ok("状态已更新", null);
    }
}
