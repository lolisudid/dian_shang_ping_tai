package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.dto.CartUpdateRequest;
import com.ecommerce.entity.CartItem;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车接口：增删改查，需登录。
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<List<CartItem>> list() {
        return Result.ok(cartService.listMyCart());
    }

    @PostMapping
    public Result<Void> add(@Validated @RequestBody CartRequest request) {
        cartService.add(request);
        return Result.ok("已加入购物车", null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody CartUpdateRequest request) {
        cartService.updateQuantity(id, request.getQuantity());
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        cartService.remove(id);
        return Result.ok(null);
    }
}
