package com.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class OrderStatusRequest {

    @NotBlank(message = "订单状态不能为�?)
    private String status;
}
