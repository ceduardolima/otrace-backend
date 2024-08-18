package com.celc.otrace.domain.utils;

public record ErrorDto(
    int statusCode,
    String message
) {
    
}
