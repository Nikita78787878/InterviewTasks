package org.example.FilterChain;

public class Request {

    private final String path;
    private final String user;

    public Request(String path, String user) {
        this.path = path;
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public String getUser() {
        return user;
    }

}
