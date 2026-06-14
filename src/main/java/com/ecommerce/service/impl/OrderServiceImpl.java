package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.ShopOrder;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.CartItemMapper;
import com.ecommerce.mapper.OrderItemMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.mapper.ShopOrderMapper;
import com.ecommerce.service.OrderService;
import com.ecommerce.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 订单服务实现 — 基于 MyBatis Plus。
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Set<String> ALLOWED_STATUS = new HashSet<>(
            Arrays.asList("PENDING", "SHIPPED", "COMPLETED", "CANCELLED"));

    private final ShopOrderMapper shopOrderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(ShopOrderMapper shopOrderMapper, OrderItemMapper orderItemMapper,
                            CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.shopOrderMapper = shopOrderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopOrder submitFromCart() {
        Long userId = UserContext.currentUserId();
        List<CartItem> cart = cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
        if (cart.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (CartItem c : cart) {
            if (c.getProductStock() < c.getQuantity()) {
                throw new BusinessException("商品【" + c.getProductName() + "】库存不足");
            }
            int updated = productMapper.decreaseStock(c.getProductId(), c.getQuantity());
            if (updated == 0) {
                throw new BusinessException("扣减库存失败：" + c.getProductName());
            }
            BigDecimal line = c.getProductPrice().multiply(BigDecimal.valueOf(c.getQuantity()));
            total = total.add(line);
            OrderItem oi = new OrderItem();
            oi.setProductId(c.getProductId());
            oi.setProductName(c.getProductName());
            oi.setPrice(c.getProductPrice());
            oi.setQuantity(c.getQuantity());
            items.add(oi);
        }
        ShopOrder order = new ShopOrder();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        shopOrderMapper.insert(order);
        for (OrderItem oi : items) {
            oi.setOrderId(order.getId());
        }
        for (OrderItem oi : items) {
            orderItemMapper.insert(oi);
        }
        cartItemMapper.delete(new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
        return detail(order.getId());
    }

    @Override
    public Page<ShopOrder> myOrders(int page, int size) {
        Long userId = UserContext.currentUserId();
        Page<ShopOrder> p = shopOrderMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<ShopOrder>()
                        .eq(ShopOrder::getUserId, userId)
                        .orderByDesc(ShopOrder::getId));
        for (ShopOrder o : p.getRecords()) {
            o.setItems(orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, o.getId())));
        }
        return p;
    }

    @Override
    public ShopOrder detail(Long id) {
        ShopOrder order = shopOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!UserContext.isAdmin() && !order.getUserId().equals(UserContext.currentUserId())) {
            throw new BusinessException(403, "无权查看该订单");
        }
        order.setItems(orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)));
        return order;
    }

    @Override
    public Page<ShopOrder> adminList(int page, int size) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
        Page<ShopOrder> p = shopOrderMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<ShopOrder>().orderByDesc(ShopOrder::getId));
        for (ShopOrder o : p.getRecords()) {
            o.setItems(orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, o.getId())));
        }
        return p;
    }

    @Override
    public void updateStatus(Long id, String status) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
        if (!ALLOWED_STATUS.contains(status)) {
            throw new BusinessException("非法订单状态");
        }
        ShopOrder order = shopOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(status);
        shopOrderMapper.updateById(order);
    }
}
