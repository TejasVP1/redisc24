package com.redisc24.redisc24.services;

import com.redisc24.redisc24.services.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final RateLimiterService fixedWindowLimiter;
    private final TokenBucketRateLimiterService tokenBucketLimiter;

    public RateLimiterInterceptor(RateLimiterService fixedWindowLimiter, TokenBucketRateLimiterService tokenBucketLimiter) {
        this.fixedWindowLimiter = fixedWindowLimiter;
        this.tokenBucketLimiter = tokenBucketLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientId = request.getRemoteAddr(); // Use IP as identifier

        if (request.getRequestURI().contains("/hello-token")) {

            if (!tokenBucketLimiter.isAllowed(clientId)) {
                response.setStatus(429);
                response.getWriter().write("Rate limit exceeded (Token Bucket). Try again later.");
                return false;
            }
        } else {

            if (!fixedWindowLimiter.isAllowed(clientId)) {
                response.setStatus(429);
                response.getWriter().write("Rate limit exceeded (Fixed Window). Try again later.");
                return false;
            }
        }
        return true;
    }
}
