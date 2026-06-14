package com.ecommerce.service;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;

import java.util.Map;

public interface AuthService {

    void register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);
}
