package com.ecommerce.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AiDescriptionRequest {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String category;
}
