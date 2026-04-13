package org.example.FilterChain;

public class AuthFilter implements Filter {
    private int order = 0;

    public AuthFilter() {
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("AuthFilter user" + request.getUser());
        if(request.getUser() == null){
            response.setStatus(401);
        } else {
            chain.doFilter(request, response);
        }
    }
}