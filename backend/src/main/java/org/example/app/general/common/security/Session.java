package org.example.app.general.common.security;

public record Session (
    String jwt,
    String originalUrl
 ) {

}
