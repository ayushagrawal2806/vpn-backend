package com.vpn.vpn_backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "success",
        "message",
        "data",
        "timestamp"
})
public class ApiResponse<T> {

    @Builder.Default
    private boolean success = true;
    private String message;
    private T data;
    @Builder.Default
    private Instant timestamp = Instant.now();

}