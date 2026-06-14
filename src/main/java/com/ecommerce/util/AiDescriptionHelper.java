package com.ecommerce.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiDescriptionHelper {

    private final boolean enabled;

    public AiDescriptionHelper(@Value("${ai.description.enabled:true}") boolean enabled) {
        this.enabled = enabled;
    }

    public String generate(String name, String category) {
        if (!enabled) {
            return name + " 暂无描述。";
        }
        String cat = category != null && !category.isEmpty() ? category : "精选";
        return String.format(
                "【%s】属于%s类目，品质可靠、性价比出色。适合日常选购，欢迎加入购物车体验。",
                name, cat);
    }
}
