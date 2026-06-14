package com.ecommerce.service.impl;

import com.ecommerce.common.PageResult;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Set<String> ALLOWED_STATUS = new HashSet<>(
            Arrays.asList("PENDING", "SHIPPED", "COMPLETED", "CANCELLED"));

    @Autowired
    private ShopOrderMapper shopOrderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopOrder submitFromCart() {
        Long userId = UserContext.currentUserId();
        List<CartItem> cart = cartItemMapper.listByUserId(userId);
        if (cart.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (CartItem c : cart) {
            if (c.getProductStock() < c.getQuantity()) {
                throw new BusinessException("商品[" + c.getProductName() + "]库存不足");
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
        orderItemMapper.insertBatch(items);
        cartItemMapper.deleteByUserId(userId);
        return detail(order.getId());
    }

    @Override
    public PageResult<ShopOrder> myOrders(int page, int size, String status) {
        Long userId = UserContext.currentUserId();
        if (status != null && status.trim().isEmpty()) {
            status = null;
        }
        if (status != null && !ALLOWED_STATUS.contains(status)) {
            throw new BusinessException("无效的订单状态筛选条件");
        }
        int offset = (page - 1) * size;
        long total = shopOrderMapper.countByUserId(userId, status);
        List<ShopOrder> records = shopOrderMapper.listByUserId(userId, status, offset, size);
        for (ShopOrder o : records) {
            o.setItems(orderItemMapper.listByOrderId(o.getId()));
        }
        return new PageResult<>(total, page, size, records);
    }

    @Override
    public ShopOrder detail(Long id) {
        ShopOrder order = shopOrderMapper.findById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!UserContext.isAdmin() && !order.getUserId().equals(UserContext.currentUserId())) {
            throw new BusinessException(403, "无权查看该订单");
        }
        order.setItems(orderItemMapper.listByOrderId(id));
        return order;
    }

    @Override
    public PageResult<ShopOrder> adminList(int page, int size) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
        int offset = (page - 1) * size;
        long total = shopOrderMapper.countAll();
        List<ShopOrder> records = shopOrderMapper.listAll(offset, size);
        for (ShopOrder o : records) {
            o.setItems(orderItemMapper.listByOrderId(o.getId()));
        }
        return new PageResult<>(total, page, size, records);
    }

    @Override
    public void updateStatus(Long id, String status) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
        if (!ALLOWED_STATUS.contains(status)) {
            throw new BusinessException("非法订单状态");
        }
        ShopOrder order = shopOrderMapper.findById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        shopOrderMapper.updateStatus(id, status);
    }
}
