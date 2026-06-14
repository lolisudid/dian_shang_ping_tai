package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.dto.CartUpdateRequest;
import com.ecommerce.entity.CartItem;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车接口。
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<List<CartItem>> list() {
        return Result.ok(cartService.listMyCart());
    }

    @PostMapping
    public Result<Void> add(@Valid @RequestBody CartRequest request) {
        cartService.add(request);
        return Result.ok("已加入购物车", null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CartUpdateRequest request) {
        cartService.updateQuantity(id, request.getQuantity());
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        cartService.remove(id);
        return Result.ok(null);
    }
}
