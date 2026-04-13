package org.example.FilterChain;

public class LoggingFilter implements Filter {
    private int order = Integer.MIN_VALUE;

    public LoggingFilter() {
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("LoggingFilter path" + request.getPath());
        chain.doFilter(request, response);

    }
}
