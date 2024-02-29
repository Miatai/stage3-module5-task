package com.mjc.school.controller.handler;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String errorCode,
    String errorMessage
) {

}
