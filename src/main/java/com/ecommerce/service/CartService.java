package com.ecommerce.service;

import com.ecommerce.dto.CartRequest;
import com.ecommerce.entity.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> listMyCart();

    void add(CartRequest request);

    void updateQuantity(Long cartItemId, Integer quantity);

    void remove(Long cartItemId);
}
