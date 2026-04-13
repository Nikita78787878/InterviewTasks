package org.example.FilterChain;


import org.example.RateLimiter.RateLimiter;

public class RateLimitFilter implements Filter {

    private final RateLimiter rateLimiter;
    private final int order = 10;

    public RateLimitFilter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        String key = request.getPath(); // можно user+path, если нужно

        boolean allowed = rateLimiter.allowRequest(key);

        if (!allowed) {
            System.out.println("Rate limit exceeded for: " + key);
            response.setStatus(429);
            return; // ⛔ стоп цепочки
        }

        System.out.println("RateLimitFilter passed");
        chain.doFilter(request, response);
    }
}