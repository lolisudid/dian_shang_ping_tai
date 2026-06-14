package com.ecommerce.service;

import com.ecommerce.common.PageResult;
import com.ecommerce.entity.ShopOrder;

public interface OrderService {

    ShopOrder submitFromCart();

    PageResult<ShopOrder> myOrders(int page, int size, String status);

    ShopOrder detail(Long id);

    PageResult<ShopOrder> adminList(int page, int size);

    void updateStatus(Long id, String status);
}
