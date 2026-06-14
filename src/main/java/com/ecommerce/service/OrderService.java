package com.ecommerce.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.entity.ShopOrder;

/**
 * 订单服务接口。
 */
public interface OrderService {

    /** 从购物车提交订单（事务扣库存） */
    ShopOrder submitFromCart();

    /** 用户分页查询自己的订单 */
    Page<ShopOrder> myOrders(int page, int size);

    /** 订单详情（含明细） */
    ShopOrder detail(Long id);

    /** 管理员分页查询所有订单 */
    Page<ShopOrder> adminList(int page, int size);

    /** 管理员更新订单状态 */
    void updateStatus(Long id, String status);
}
