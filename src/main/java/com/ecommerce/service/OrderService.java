package com.ecommerce.service;

import com.ecommerce.common.PageResult;
import com.ecommerce.entity.ShopOrder;

/**
 * 订单服务接口。
 */
public interface OrderService {

    ShopOrder submitFromCart();

    PageResult<ShopOrder> myOrders(int page, int size);

    ShopOrder detail(Long id);

    PageResult<ShopOrder> adminList(int page, int size);

    void updateStatus(Long id, String status);
}
