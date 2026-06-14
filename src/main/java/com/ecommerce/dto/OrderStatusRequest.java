package com.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class OrderStatusRequest {

    @NotBlank(message = "状态不能为空")
    private String status;
}