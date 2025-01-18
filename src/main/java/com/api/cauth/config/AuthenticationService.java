package com.api.cauth.config;

import com.api.cauth.entities.Product;
import com.api.cauth.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "accessKey";

    public static Authentication getAuthentication(HttpServletRequest request, ProductRepository productRepository) {
        String accessKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (!request.getRequestURL().toString().endsWith("/save-product")) {
            if (!verifyAccessToken(accessKey, productRepository)) {
                throw new BadCredentialsException("Invalid accessKey");
            }
        }

        return new ApiKeyAuthentication(accessKey, AuthorityUtils.NO_AUTHORITIES);
    }

    private static boolean verifyAccessToken(String accessKey, ProductRepository productRepository) {
        if (!productRepository.findAll().isEmpty()) {
            for (Product product : productRepository.findAll()) {
                if (accessKey != null && accessKey.equals(product.getAccessKey())) {
                    return true;
                }
            }
        }

        return false;

    }
}
