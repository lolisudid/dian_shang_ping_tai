package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.CartItemMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.CartService;
import com.ecommerce.util.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务实现。
 */
@Service
public class CartServiceImpl implements CartService {

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<CartItem> listMyCart() {
        return cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, UserContext.currentUserId()));
    }

    @Override
    public void add(CartRequest request) {
        Long userId = UserContext.currentUserId();
        Product product = productMapper.selectById(request.getProductId());
        if (product == null || product.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException("库存不足");
        }
        CartItem exist = cartItemMapper.selectOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, request.getProductId()));
        if (exist != null) {
            int newQty = exist.getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new BusinessException("库存不足");
            }
            exist.setQuantity(newQty);
            cartItemMapper.updateById(exist);
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
        CartItem target = cartItemMapper.selectById(cartItemId);
        if (target == null || !target.getUserId().equals(UserContext.currentUserId())) {
            throw new BusinessException("购物车项不存在");
        }
        if (quantity <= 0) {
            cartItemMapper.deleteById(cartItemId);
            return;
        }
        Product product = productMapper.selectById(target.getProductId());
        if (product.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        target.setQuantity(quantity);
        cartItemMapper.updateById(target);
    }

    @Override
    public void remove(Long cartItemId) {
        cartItemMapper.deleteById(cartItemId);
    }
}
