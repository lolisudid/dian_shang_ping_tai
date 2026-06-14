package com.ecommerce.service.impl;

import com.ecommerce.dto.CartRequest;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.CartItemMapper;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private ProductService productService;

    @Override
    public List<CartItem> listMyCart() {
        return cartItemMapper.listByUserId(UserContext.currentUserId());
    }

    @Override
    public void add(CartRequest request) {
        Long userId = UserContext.currentUserId();
        Product product = productService.getById(request.getProductId());
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException("库存不足");
        }
        CartItem exist = cartItemMapper.findByUserAndProduct(userId, request.getProductId());
        if (exist != null) {
            int newQty = exist.getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new BusinessException("库存不足");
            }
            cartItemMapper.updateQuantity(exist.getId(), newQty);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(request.getProductId());
            item.setQuantity(request.getQuantity());
            cartItemMapper.insert(item);
        }
    }

    @Override
    public void updateQuantity(Long cartItemId, Integer quantity) {
        Long userId = UserContext.currentUserId();
        List<CartItem> items = cartItemMapper.listByUserId(userId);
        CartItem target = items.stream().filter(i -> i.getId().equals(cartItemId)).findFirst()
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        if (quantity <= 0) {
            cartItemMapper.deleteById(cartItemId);
            return;
        }
        if (target.getProductStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        cartItemMapper.updateQuantity(cartItemId, quantity);
    }

    @Override
    public void remove(Long cartItemId) {
        cartItemMapper.deleteById(cartItemId);
    }
}
