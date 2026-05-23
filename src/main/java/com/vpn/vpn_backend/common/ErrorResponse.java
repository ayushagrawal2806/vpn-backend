package com.vpn.vpn_backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "success",
        "status",
        "errorCode",
        "message",
        "errors",
        "timestamp"
})
public class ErrorResponse {
    @Builder.Default
    private boolean success = false;
    private String message;
    private String errorCode;
    private int status;
    private Map<String, String> errors;
    @Builder.Default
    private Instant timestamp = Instant.now();

}