package org.example.FilterChain;

public interface Filter {

    int getOrder();

    void doFilter(Request request, Response response, FilterChain chain);

}