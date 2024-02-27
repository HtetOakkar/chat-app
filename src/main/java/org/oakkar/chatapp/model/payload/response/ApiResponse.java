package org.oakkar.chatapp.model.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private String message;
    private boolean success;
    private T data;

    public static  <T> ApiResponse<T> success(T data, String message) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(T data, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }

    public  static <T> ApiResponse<T> empty() {
        return success(null, null);
    }

    public static  <T> ApiResponse<T> success(String message) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

}
