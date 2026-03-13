package org.example.app.general.common.security;

public class Session {
    private String jwt;
    private String originalUrl;

    public Session() {
    }

    // Constructor
    public Session(String jwt, String originalUrl) {
        this.jwt = jwt;
        this.originalUrl = originalUrl;
    }

    // Getters and setters
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
